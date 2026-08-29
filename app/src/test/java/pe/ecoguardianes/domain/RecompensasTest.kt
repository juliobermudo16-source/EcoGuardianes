package pe.ecoguardianes.domain

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import pe.ecoguardianes.data.catalogo.CatalogoColeccion
import pe.ecoguardianes.data.catalogo.CatalogoInsignias
import pe.ecoguardianes.domain.audit.DesbloqueoZonas
import pe.ecoguardianes.domain.audit.EstadoJuego
import pe.ecoguardianes.domain.audit.EvaluadorRecompensas
import pe.ecoguardianes.domain.audit.Medidor
import pe.ecoguardianes.domain.audit.ResumenZona
import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.EstadoZona
import pe.ecoguardianes.domain.model.Medida
import pe.ecoguardianes.domain.model.Requisito
import pe.ecoguardianes.domain.model.ZonaId

class RecompensasTest {

    private fun juego(
        xp: Int = 0,
        residuos: Int = 0,
        agua: Int = 0,
        auditorias: Int = 0,
        acciones: Int = 0,
        perfectas: Int = 0,
        maximo: Int = 0,
        zonas: Map<ZonaId, ResumenZona> = emptyMap()
    ) = EstadoJuego(
        xpTotal = xp,
        hallazgosPorCategoria = mapOf(
            Categoria.RESIDUOS to residuos,
            Categoria.AGUA to agua
        ),
        accionesCorrectas = acciones,
        auditoriasCompletadas = auditorias,
        deteccionesPerfectas = perfectas,
        puntajeMaximo = maximo,
        zonas = zonas
    )

    @Test
    fun `el medidor traduce cada requisito a un valor real`() {
        val estado = juego(xp = 800, residuos = 9, agua = 3, auditorias = 4, acciones = 11)

        assertEquals(
            9,
            Medidor.valor(estado, Requisito(Medida.HALLAZGOS_CATEGORIA, 8, Categoria.RESIDUOS))
        )
        assertEquals(12, Medidor.valor(estado, Requisito(Medida.HALLAZGOS_TOTAL, 1)))
        assertEquals(4, Medidor.valor(estado, Requisito(Medida.AUDITORIAS, 1)))
        assertEquals(11, Medidor.valor(estado, Requisito(Medida.ACCIONES_CORRECTAS, 1)))
        assertEquals(800, Medidor.valor(estado, Requisito(Medida.XP_TOTAL, 1)))
    }

    @Test
    fun `el progreso hacia un requisito nunca se sale del rango cero uno`() {
        val estado = juego(residuos = 100)
        val requisito = Requisito(Medida.HALLAZGOS_CATEGORIA, 8, Categoria.RESIDUOS)

        assertEquals(1f, Medidor.progreso(estado, requisito), 0.001f)
        assertEquals(0f, Medidor.progreso(juego(), requisito), 0.001f)
    }

    @Test
    fun `una insignia se desbloquea solo al cumplir su requisito`() {
        val insignia = CatalogoInsignias.insignia("I_RESIDUOS")
        assertNotNull(insignia)
        val meta = insignia!!.requisito.meta

        val casi = EvaluadorRecompensas.evaluarInsignias(
            juego(residuos = meta - 1),
            listOf(insignia)
        ).first()
        val justo = EvaluadorRecompensas.evaluarInsignias(
            juego(residuos = meta),
            listOf(insignia)
        ).first()

        assertFalse(casi.desbloqueada)
        assertTrue(justo.desbloqueada)
    }

    @Test
    fun `sin progreso no hay ninguna insignia desbloqueada`() {
        val progresos = EvaluadorRecompensas.evaluarInsignias(
            EstadoJuego(),
            CatalogoInsignias.insignias
        )
        assertEquals(CatalogoInsignias.insignias.size, progresos.size)
        assertTrue(progresos.none { it.desbloqueada })
    }

    @Test
    fun `solo se notifican los desbloqueos que son nuevos`() {
        val insignia = CatalogoInsignias.insignia("I_RESIDUOS")!!
        val progresos = EvaluadorRecompensas.evaluarInsignias(
            juego(residuos = 50),
            listOf(insignia)
        )

        assertEquals(listOf("I_RESIDUOS"), EvaluadorRecompensas.nuevosDesbloqueos(progresos, emptySet()))
        assertTrue(
            EvaluadorRecompensas.nuevosDesbloqueos(progresos, setOf("I_RESIDUOS")).isEmpty()
        )
    }

    @Test
    fun `el coleccionable que depende de la coleccion se evalua en segunda pasada`() {
        val estadoCompleto = EstadoJuego(
            xpTotal = 5000,
            hallazgosPorCategoria = Categoria.entries.associateWith { 50 },
            accionesCorrectas = 100,
            auditoriasCompletadas = 30,
            deteccionesPerfectas = 10,
            puntajeMaximo = 100,
            zonas = ZonaId.entries.associateWith { ResumenZona(it, estrellas = 3, mejorPuntaje = 100) }
        )
        val progresos = EvaluadorRecompensas.evaluarColeccionables(
            estadoCompleto,
            CatalogoColeccion.coleccionables
        )
        val vitrina = progresos.first { it.id == "C_VITRINA" }

        assertTrue(vitrina.desbloqueada)
        assertEquals(CatalogoColeccion.coleccionables.size, progresos.size)
    }

    @Test
    fun `la primera zona siempre esta disponible y la segunda empieza bloqueada`() {
        val inicial = EstadoJuego()

        assertEquals(EstadoZona.DISPONIBLE, DesbloqueoZonas.estado(ZonaId.CASA, inicial))
        assertEquals(EstadoZona.BLOQUEADA, DesbloqueoZonas.estado(ZonaId.ESCUELA, inicial))
        assertNull(DesbloqueoZonas.requisitoPendiente(ZonaId.CASA, inicial))
        assertNotNull(DesbloqueoZonas.requisitoPendiente(ZonaId.ESCUELA, inicial))
    }

    @Test
    fun `una zona se abre al completar la anterior y tener el XP necesario`() {
        val conCasa = juego(
            xp = 200,
            zonas = mapOf(ZonaId.CASA to ResumenZona(ZonaId.CASA, estrellas = 2, mejorPuntaje = 80))
        )

        assertTrue(DesbloqueoZonas.estaAbierta(ZonaId.ESCUELA, conCasa))
        assertEquals(EstadoZona.DISPONIBLE, DesbloqueoZonas.estado(ZonaId.ESCUELA, conCasa))
        assertEquals(EstadoZona.COMPLETADA, DesbloqueoZonas.estado(ZonaId.CASA, conCasa))
    }

    @Test
    fun `sin XP suficiente la zona sigue bloqueada aunque se complete la anterior`() {
        val estado = juego(
            xp = 10,
            zonas = mapOf(ZonaId.CASA to ResumenZona(ZonaId.CASA, estrellas = 3))
        )

        assertFalse(DesbloqueoZonas.estaAbierta(ZonaId.ESCUELA, estado))
        assertTrue(
            DesbloqueoZonas.requisitoPendiente(ZonaId.ESCUELA, estado)!!.contains("XP")
        )
    }

    @Test
    fun `tres estrellas marcan la zona como dominada`() {
        val estado = juego(
            zonas = mapOf(ZonaId.CASA to ResumenZona(ZonaId.CASA, estrellas = 3, mejorPuntaje = 95))
        )
        assertEquals(EstadoZona.DOMINADA, DesbloqueoZonas.estado(ZonaId.CASA, estado))
    }

    @Test
    fun `una zona iniciada sin estrellas aparece en progreso`() {
        val estado = juego(
            zonas = mapOf(ZonaId.CASA to ResumenZona(ZonaId.CASA, estrellas = 0, iniciada = true))
        )
        assertEquals(EstadoZona.EN_PROGRESO, DesbloqueoZonas.estado(ZonaId.CASA, estado))
    }

    @Test
    fun `el estado de todas las zonas devuelve las seis zonas del mapa`() {
        val mapa = DesbloqueoZonas.estadoDeTodas(EstadoJuego())
        assertEquals(6, mapa.size)
        assertTrue(mapa.values.count { it == EstadoZona.BLOQUEADA } == 5)
    }
}
