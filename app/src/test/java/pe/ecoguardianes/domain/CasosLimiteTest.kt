package pe.ecoguardianes.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.ecoguardianes.domain.audit.DesbloqueoZonas
import pe.ecoguardianes.domain.audit.EstadoJuego
import pe.ecoguardianes.domain.audit.EvaluadorRecompensas
import pe.ecoguardianes.domain.audit.MarcaJugador
import pe.ecoguardianes.domain.audit.Medidor
import pe.ecoguardianes.domain.audit.MotorAuditoria
import pe.ecoguardianes.domain.audit.Progresion
import pe.ecoguardianes.domain.audit.ResumenZona
import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.DestinoReto
import pe.ecoguardianes.domain.model.Gravedad
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.domain.model.Medida
import pe.ecoguardianes.domain.model.PiezaReto
import pe.ecoguardianes.domain.model.Requisito
import pe.ecoguardianes.domain.model.Reto
import pe.ecoguardianes.domain.model.TipoReto
import pe.ecoguardianes.domain.model.ZonaId

/** Casos límite exigidos por el plan de pruebas. */
class CasosLimiteTest {

    @Test
    fun `un texto muy largo no rompe el modelo de situacion`() {
        val largo = "á".repeat(5000)
        val situacion = Fixtures.situacion("S1").copy(
            nombre = largo,
            observacion = largo,
            explicacion = largo,
            pista = largo
        )
        val evaluacion = MotorAuditoria.evaluarHallazgo(
            situacion,
            MarcaJugador("S1", true, Categoria.RESIDUOS, Gravedad.NO_CONFORMIDAD)
        )

        assertTrue(evaluacion.deteccionCorrecta)
        assertEquals(5000, situacion.nombre.length)
    }

    @Test
    fun `un texto vacio no impide evaluar el hallazgo`() {
        val situacion = Fixtures.situacion("S1").copy(
            nombre = "",
            observacion = "",
            explicacion = "",
            pista = ""
        )
        val resultado = MotorAuditoria.calcular(
            Fixtures.mision(listOf(situacion)),
            listOf(situacion),
            listOf(MarcaJugador("S1", true))
        )

        assertEquals(1, resultado.detectadosCorrectos)
        assertEquals(0, resultado.clasificacionesCorrectas)
    }

    @Test
    fun `un XP negativo en la situacion no genera puntuaciones negativas`() {
        val situacion = Fixtures.situacion("S1", xp = -50)
        val resultado = MotorAuditoria.calcular(
            Fixtures.mision(listOf(situacion)),
            listOf(situacion),
            listOf(
                MarcaJugador(
                    "S1", true, Categoria.RESIDUOS, Gravedad.NO_CONFORMIDAD,
                    accionElegidaId = "x", accionAcertada = true
                )
            )
        )

        assertTrue(resultado.xpGanado >= 0)
        assertTrue(resultado.puntaje in 0..100)
    }

    @Test
    fun `el puntaje siempre queda entre cero y cien`() {
        val situaciones = (1..40).map {
            Fixtures.situacion("C$it", gravedad = Gravedad.CONFORME)
        }
        val marcas = situaciones.map { MarcaJugador(it.id, true) }
        val resultado = MotorAuditoria.calcular(
            Fixtures.mision(situaciones, minimo = 0),
            situaciones,
            marcas
        )

        assertTrue(resultado.puntaje in 0..100)
        assertEquals(40, resultado.falsosPositivos)
        assertEquals(0, resultado.conformes)
    }

    @Test
    fun `una mision completada al maximo entrega el progreso maximo`() {
        val situaciones = listOf(Fixtures.situacion("S1"), Fixtures.situacion("S2"))
        val marcas = situaciones.map {
            MarcaJugador(
                it.id, true, it.categoria, it.gravedad,
                accionElegidaId = it.id + "_A1", accionAcertada = true
            )
        }
        val resultado = MotorAuditoria.calcular(
            Fixtures.mision(situaciones, minimo = 2),
            situaciones,
            marcas
        )

        assertEquals(100, resultado.puntaje)
        assertEquals(3, resultado.estrellas)
        assertEquals(0, resultado.omitidos)
        assertTrue(resultado.aprobada)
    }

    @Test
    fun `una mision incompleta se puede repetir sin perder lo conseguido`() {
        val situaciones = listOf(Fixtures.situacion("S1"), Fixtures.situacion("S2"))
        val flojo = MotorAuditoria.calcular(
            Fixtures.mision(situaciones, minimo = 1),
            situaciones,
            listOf(MarcaJugador("S1", true))
        )
        val bueno = MotorAuditoria.calcular(
            Fixtures.mision(situaciones, minimo = 1),
            situaciones,
            situaciones.map {
                MarcaJugador(
                    it.id, true, it.categoria, it.gravedad,
                    accionElegidaId = it.id + "_A1", accionAcertada = true
                )
            }
        )

        assertTrue(bueno.puntaje > flojo.puntaje)
        assertEquals(maxOf(flojo.estrellas, bueno.estrellas), bueno.estrellas)
    }

    @Test
    fun `un requisito con meta cero se considera cumplido sin dividir por cero`() {
        val requisito = Requisito(Medida.HALLAZGOS_TOTAL, 0)
        assertEquals(1f, Medidor.progreso(EstadoJuego(), requisito), 0.001f)
        assertTrue(Medidor.cumplido(EstadoJuego(), requisito))
    }

    @Test
    fun `un requisito de categoria sin categoria devuelve cero`() {
        val requisito = Requisito(Medida.HALLAZGOS_CATEGORIA, 5, categoria = null)
        assertEquals(0, Medidor.valor(EstadoJuego(), requisito))
        assertNotNull(requisito.descripcion)
    }

    @Test
    fun `una lista vacia de recompensas no falla`() {
        assertTrue(EvaluadorRecompensas.evaluarInsignias(EstadoJuego(), emptyList()).isEmpty())
        assertTrue(EvaluadorRecompensas.evaluarColeccionables(EstadoJuego(), emptyList()).isEmpty())
        assertTrue(EvaluadorRecompensas.nuevosDesbloqueos(emptyList(), emptySet()).isEmpty())
    }

    @Test
    fun `un progreso de zona corrupto no rompe el mapa`() {
        val estado = EstadoJuego(
            xpTotal = -100,
            zonas = mapOf(ZonaId.CASA to ResumenZona(ZonaId.CASA, estrellas = 99))
        )
        val mapa = DesbloqueoZonas.estadoDeTodas(estado)

        assertEquals(6, mapa.size)
        assertEquals(1, Progresion.nivel(estado.xpTotal))
    }

    @Test
    fun `un reto sin piezas nunca se da por resuelto`() {
        val reto = Reto(
            id = "VACIO",
            tipo = TipoReto.SELECCION,
            enunciado = "",
            ayuda = "",
            piezas = emptyList(),
            destinos = emptyList()
        )

        assertFalse(reto.esSolucionCorrecta(emptyMap()))
        assertFalse(reto.esSolucionCorrecta(mapOf("a" to "b")))
        assertEquals(0, reto.aciertos(emptyMap()))
    }

    @Test
    fun `colocar una pieza en un destino inexistente cuenta como error`() {
        val reto = Reto(
            id = "R",
            tipo = TipoReto.CLASIFICAR,
            enunciado = "e",
            ayuda = "a",
            piezas = listOf(PiezaReto("p1", "Pieza", IconoAmb.BOTELLA, "d1")),
            destinos = listOf(DestinoReto("d1", "Destino", IconoAmb.CONTENEDOR, 0xFF000000))
        )

        assertFalse(reto.esSolucionCorrecta(mapOf("p1" to "inexistente")))
        assertEquals(0, reto.aciertos(mapOf("p1" to "inexistente")))
        assertTrue(reto.esSolucionCorrecta(mapOf("p1" to "d1")))
    }

    @Test
    fun `todas las categorias y gravedades tienen etiqueta simbolo y color`() {
        Categoria.entries.forEach {
            assertTrue(it.etiqueta.isNotBlank())
            assertTrue(it.simbolo.isNotBlank())
            assertTrue(it.explicacion.isNotBlank())
            assertTrue(it.colorHex != 0L)
            assertEquals(it, Categoria.porId(it.name))
        }
        Gravedad.entries.forEach {
            assertTrue(it.etiqueta.isNotBlank())
            assertTrue(it.simbolo.isNotBlank())
            assertTrue(it.descripcion.isNotBlank())
        }
        assertFalse(Gravedad.CONFORME.esProblema)
        assertTrue(Gravedad.OBSERVACION.esProblema)
        assertTrue(Gravedad.NO_CONFORMIDAD.esProblema)
    }

    @Test
    fun `identificadores desconocidos devuelven nulo en los enumerados`() {
        assertEquals(null, Categoria.porId("NO_EXISTE"))
        assertEquals(null, ZonaId.porId(""))
        assertEquals(6, ZonaId.enOrden.size)
    }

    @Test
    fun `todos los iconos ambientales tienen descripcion accesible`() {
        IconoAmb.entries.forEach {
            val descripcion = pe.ecoguardianes.ui.art.descripcionDe(it)
            assertTrue("Falta descripción de " + it.name, descripcion.isNotBlank())
        }
    }
}
