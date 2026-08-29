package pe.ecoguardianes.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.ecoguardianes.domain.audit.MarcaJugador
import pe.ecoguardianes.domain.audit.MotorAuditoria
import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.Gravedad

class MotorAuditoriaTest {

    private fun marcaCompleta(
        id: String,
        categoria: Categoria = Categoria.RESIDUOS,
        gravedad: Gravedad = Gravedad.NO_CONFORMIDAD
    ) = MarcaJugador(
        situacionId = id,
        marcadaComoProblema = true,
        categoriaElegida = categoria,
        gravedadElegida = gravedad,
        accionElegidaId = id + "_A1",
        accionAcertada = true
    )

    @Test
    fun `detectar un problema real cuenta como hallazgo valido`() {
        val situacion = Fixtures.situacion("S1")
        val evaluacion = MotorAuditoria.evaluarHallazgo(situacion, marcaCompleta("S1"))

        assertTrue(evaluacion.deteccionCorrecta)
        assertTrue(evaluacion.esHallazgoValido)
        assertFalse(evaluacion.esFalsoPositivo)
        assertFalse(evaluacion.esOmision)
    }

    @Test
    fun `marcar una situacion correcta genera un falso positivo`() {
        val situacion = Fixtures.situacion("S1", gravedad = Gravedad.CONFORME)
        val evaluacion = MotorAuditoria.evaluarHallazgo(
            situacion,
            MarcaJugador("S1", marcadaComoProblema = true)
        )

        assertTrue(evaluacion.esFalsoPositivo)
        assertFalse(evaluacion.esHallazgoValido)
        assertEquals(0, evaluacion.xpObtenido)
    }

    @Test
    fun `no marcar un problema real cuenta como omision`() {
        val situacion = Fixtures.situacion("S1")
        val evaluacion = MotorAuditoria.evaluarHallazgo(situacion, null)

        assertTrue(evaluacion.esOmision)
        assertFalse(evaluacion.deteccionCorrecta)
    }

    @Test
    fun `la clasificacion errónea no suma XP de clasificacion`() {
        val situacion = Fixtures.situacion("S1", categoria = Categoria.AGUA, xp = 20)
        val evaluacion = MotorAuditoria.evaluarHallazgo(
            situacion,
            MarcaJugador(
                "S1",
                marcadaComoProblema = true,
                categoriaElegida = Categoria.RUIDO,
                gravedadElegida = Gravedad.NO_CONFORMIDAD
            )
        )

        assertFalse(evaluacion.categoriaCorrecta)
        assertTrue(evaluacion.gravedadCorrecta)
        assertEquals(20 + MotorAuditoria.XP_CLASIFICACION, evaluacion.xpObtenido)
    }

    @Test
    fun `auditoria perfecta alcanza el puntaje maximo`() {
        val situaciones = listOf(
            Fixtures.situacion("S1"),
            Fixtures.situacion("S2"),
            Fixtures.situacion("S3", gravedad = Gravedad.CONFORME)
        )
        val marcas = listOf(marcaCompleta("S1"), marcaCompleta("S2"))
        val resultado = MotorAuditoria.calcular(Fixtures.mision(situaciones), situaciones, marcas)

        assertEquals(100, resultado.puntaje)
        assertEquals(3, resultado.estrellas)
        assertTrue(resultado.deteccionPerfecta)
        assertTrue(resultado.aprobada)
    }

    @Test
    fun `auditoria sin ninguna marca da puntaje bajo pero nunca negativo`() {
        val situaciones = listOf(Fixtures.situacion("S1"), Fixtures.situacion("S2"))
        val resultado = MotorAuditoria.calcular(
            Fixtures.mision(situaciones),
            situaciones,
            emptyList()
        )

        // Solo conserva los 5 puntos de precisión: no acusó a nadie sin motivo.
        assertEquals(5, resultado.puntaje)
        assertTrue(resultado.puntaje < MotorAuditoria.UMBRAL_UNA_ESTRELLA)
        assertEquals(0, resultado.estrellas)
        assertEquals(0, resultado.xpGanado)
        assertFalse(resultado.aprobada)
        assertEquals(2, resultado.omitidos)
    }

    @Test
    fun `los falsos positivos penalizan hasta un maximo de quince puntos`() {
        val situaciones = (1..5).map {
            Fixtures.situacion("C$it", gravedad = Gravedad.CONFORME)
        } + Fixtures.situacion("P1")
        val marcas = (1..5).map { MarcaJugador("C$it", true) } + marcaCompleta("P1")

        val resultado = MotorAuditoria.calcular(Fixtures.mision(situaciones), situaciones, marcas)

        // 100 de base menos la precisión perdida (5) menos el tope de penalización (15).
        assertEquals(80, resultado.puntaje)
        assertEquals(5, resultado.falsosPositivos)
    }

    @Test
    fun `una mision sin problemas reales se considera detectada al cien por cien`() {
        val situaciones = listOf(
            Fixtures.situacion("C1", gravedad = Gravedad.CONFORME),
            Fixtures.situacion("C2", gravedad = Gravedad.CONFORME)
        )
        val resultado = MotorAuditoria.calcular(
            Fixtures.mision(situaciones, minimo = 0),
            situaciones,
            emptyList()
        )

        assertEquals(1f, resultado.porcentajeDeteccion, 0.001f)
        assertFalse(resultado.deteccionPerfecta)
        assertTrue(resultado.puntaje > 0)
    }

    @Test
    fun `las estrellas siguen los umbrales definidos`() {
        assertEquals(3, MotorAuditoria.estrellasPara(90))
        assertEquals(3, MotorAuditoria.estrellasPara(100))
        assertEquals(2, MotorAuditoria.estrellasPara(89))
        assertEquals(2, MotorAuditoria.estrellasPara(70))
        assertEquals(1, MotorAuditoria.estrellasPara(69))
        assertEquals(1, MotorAuditoria.estrellasPara(50))
        assertEquals(0, MotorAuditoria.estrellasPara(49))
        assertEquals(0, MotorAuditoria.estrellasPara(0))
    }

    @Test
    fun `el conteo de gravedades solo cuenta lo detectado`() {
        val situaciones = listOf(
            Fixtures.situacion("S1", gravedad = Gravedad.NO_CONFORMIDAD),
            Fixtures.situacion("S2", gravedad = Gravedad.OBSERVACION),
            Fixtures.situacion("S3", gravedad = Gravedad.NO_CONFORMIDAD),
            Fixtures.situacion("S4", gravedad = Gravedad.CONFORME)
        )
        val marcas = listOf(
            marcaCompleta("S1"),
            marcaCompleta("S2", gravedad = Gravedad.OBSERVACION)
        )
        val resultado = MotorAuditoria.calcular(Fixtures.mision(situaciones), situaciones, marcas)

        assertEquals(1, resultado.noConformidades)
        assertEquals(1, resultado.observaciones)
        assertEquals(1, resultado.conformes)
        assertEquals(1, resultado.omitidos)
    }

    @Test
    fun `no se aprueba si no se alcanza el minimo de hallazgos de la mision`() {
        val situaciones = listOf(
            Fixtures.situacion("S1"),
            Fixtures.situacion("S2"),
            Fixtures.situacion("S3")
        )
        val resultado = MotorAuditoria.calcular(
            Fixtures.mision(situaciones, minimo = 3),
            situaciones,
            listOf(marcaCompleta("S1"))
        )

        assertFalse(resultado.aprobada)
        assertEquals(1, resultado.detectadosCorrectos)
    }

    @Test
    fun `una lista de situaciones vacia no rompe el calculo`() {
        val resultado = MotorAuditoria.calcular(
            Fixtures.mision(emptyList(), minimo = 0),
            emptyList(),
            emptyList()
        )

        assertEquals(0, resultado.totalSituaciones)
        assertEquals(100, resultado.puntaje)
        assertFalse(resultado.deteccionPerfecta)
    }

    @Test
    fun `las marcas duplicadas no cuentan dos veces`() {
        val situaciones = listOf(Fixtures.situacion("S1"))
        val marcas = listOf(marcaCompleta("S1"), marcaCompleta("S1"))
        val resultado = MotorAuditoria.calcular(Fixtures.mision(situaciones), situaciones, marcas)

        assertEquals(1, resultado.detectadosCorrectos)
        assertEquals(100, resultado.puntaje)
    }

    @Test
    fun `las marcas de situaciones inexistentes se ignoran`() {
        val situaciones = listOf(Fixtures.situacion("S1"))
        val marcas = listOf(marcaCompleta("S1"), marcaCompleta("FANTASMA"))
        val resultado = MotorAuditoria.calcular(Fixtures.mision(situaciones), situaciones, marcas)

        assertEquals(1, resultado.evaluaciones.size)
        assertEquals(0, resultado.falsosPositivos)
    }

    @Test
    fun `el XP ganado suma hallazgos clasificacion accion y estrellas`() {
        val situaciones = listOf(Fixtures.situacion("S1", xp = 30))
        val resultado = MotorAuditoria.calcular(
            Fixtures.mision(situaciones),
            situaciones,
            listOf(marcaCompleta("S1"))
        )

        val esperado = 30 +
            MotorAuditoria.XP_CLASIFICACION * 2 +
            MotorAuditoria.XP_ACCION_CORRECTA +
            3 * MotorAuditoria.XP_POR_ESTRELLA
        assertEquals(esperado, resultado.xpGanado)
    }

    @Test
    fun `detectar sin proponer accion baja el puntaje pero mantiene el hallazgo`() {
        val situaciones = listOf(Fixtures.situacion("S1"))
        val marca = MarcaJugador(
            "S1",
            marcadaComoProblema = true,
            categoriaElegida = Categoria.RESIDUOS,
            gravedadElegida = Gravedad.NO_CONFORMIDAD
        )
        val resultado = MotorAuditoria.calcular(
            Fixtures.mision(situaciones),
            situaciones,
            listOf(marca)
        )

        assertEquals(1, resultado.detectadosCorrectos)
        assertEquals(0, resultado.accionesCorrectas)
        // 40 de detección + 25 de clasificación + 5 de precisión.
        assertEquals(70, resultado.puntaje)
    }
}
