package pe.ecoguardianes.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.ecoguardianes.data.catalogo.CatalogoAcciones
import pe.ecoguardianes.data.catalogo.CatalogoColeccion
import pe.ecoguardianes.data.catalogo.CatalogoEscenarios
import pe.ecoguardianes.data.catalogo.CatalogoInsignias
import pe.ecoguardianes.data.local.EcoDatabase
import pe.ecoguardianes.data.repo.EcoRepositorio
import pe.ecoguardianes.domain.audit.MarcaJugador
import pe.ecoguardianes.domain.audit.MotorAuditoria
import pe.ecoguardianes.domain.model.Mision
import pe.ecoguardianes.domain.model.Situacion
import pe.ecoguardianes.domain.model.ZonaId

/** Comprueba que el progreso se guarda de verdad en Room. */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], manifest = Config.NONE)
class RepositorioRoomTest {

    private lateinit var db: EcoDatabase
    private lateinit var repo: EcoRepositorio
    private var reloj = 1_700_000_000_000L

    @Before
    fun crearBase() {
        val contexto = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(contexto, EcoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repo = EcoRepositorio(db) { reloj }
    }

    @After
    fun cerrarBase() {
        db.close()
    }

    private fun marcasPerfectas(situaciones: List<Situacion>): List<MarcaJugador> =
        situaciones.filter { it.esProblema }.map {
            MarcaJugador(
                situacionId = it.id,
                marcadaComoProblema = true,
                categoriaElegida = it.categoria,
                gravedadElegida = it.gravedad,
                accionElegidaId = CatalogoAcciones.correctaDe(it.id)?.id,
                accionAcertada = true,
                retoSuperado = it.retoId != null
            )
        }

    private suspend fun jugarZona(
        zona: ZonaId,
        marcas: (List<Situacion>) -> List<MarcaJugador> = ::marcasPerfectas
    ): Pair<Mision, Long> {
        val mision = CatalogoEscenarios.misionDe(zona)
        val situaciones = CatalogoEscenarios.deZona(zona)
        val lista = marcas(situaciones)
        val resultado = MotorAuditoria.calcular(mision, situaciones, lista)
        val guardado = repo.guardarAuditoria(mision, situaciones, lista, resultado, reloj - 60_000)
        return mision to guardado.auditoriaId
    }

    @Test
    fun `una base nueva no tiene progreso`() = runTest {
        assertNull(repo.perfil.first())
        assertTrue(repo.auditorias.first().isEmpty())
        assertEquals(0, repo.estadoJuego.first().hallazgosTotales)
        assertEquals(0, repo.estadoJuego.first().xpTotal)
    }

    @Test
    fun `asegurarPerfil crea una unica fila`() = runTest {
        val primero = repo.asegurarPerfil()
        val segundo = repo.asegurarPerfil()

        assertEquals(primero.id, segundo.id)
        assertEquals(0, primero.xp)
        assertNotNull(repo.perfil.first())
    }

    @Test
    fun `crearPerfil guarda alias y avatar y marca el onboarding`() = runTest {
        repo.crearPerfil("  Luchi  ", "AV_PEZ")
        val perfil = repo.perfil.first()!!

        assertEquals("Luchi", perfil.alias)
        assertEquals("AV_PEZ", perfil.avatarId)
        assertTrue(perfil.onboardingCompletado)
    }

    @Test
    fun `un alias vacio usa el valor por defecto`() = runTest {
        repo.crearPerfil("    ", "AV_HOJA")
        assertEquals(EcoRepositorio.ALIAS_POR_DEFECTO, repo.perfil.first()!!.alias)
    }

    @Test
    fun `un alias demasiado largo se recorta`() = runTest {
        repo.crearPerfil("G".repeat(80), "AV_HOJA")
        assertEquals(EcoRepositorio.MAX_ALIAS, repo.perfil.first()!!.alias.length)
    }

    @Test
    fun `guardar una auditoria persiste la ficha y sus hallazgos`() = runTest {
        repo.crearPerfil("Ana", "AV_HOJA")
        val (mision, id) = jugarZona(ZonaId.CASA)

        val auditoria = repo.auditoria(id)
        assertNotNull(auditoria)
        assertEquals(mision.id, auditoria!!.misionId)
        assertEquals(ZonaId.CASA.name, auditoria.zonaId)
        assertTrue(auditoria.completada)
        assertEquals(100, auditoria.puntaje)
        assertEquals(3, auditoria.estrellas)

        val hallazgos = repo.hallazgosDe(id)
        val problemas = CatalogoEscenarios.deZona(ZonaId.CASA).count { it.esProblema }
        assertEquals(problemas, hallazgos.size)
        assertTrue(hallazgos.all { it.valido })
        assertTrue(hallazgos.all { it.registradoEn == reloj })
    }

    @Test
    fun `el XP de la auditoria se suma al perfil`() = runTest {
        repo.crearPerfil("Ana", "AV_HOJA")
        val (_, id) = jugarZona(ZonaId.CASA)
        val ganado = repo.auditoria(id)!!.xpGanado

        assertTrue(ganado > 0)
        assertEquals(ganado, repo.perfil.first()!!.xp)
        assertEquals(ganado, repo.estadoJuego.first().xpTotal)
    }

    @Test
    fun `los falsos positivos se guardan marcados como no validos`() = runTest {
        repo.crearPerfil("Ana", "AV_HOJA")
        val situaciones = CatalogoEscenarios.deZona(ZonaId.CASA)
        val correcta = situaciones.first { !it.esProblema }
        val marcas = marcasPerfectas(situaciones) +
            MarcaJugador(correcta.id, marcadaComoProblema = true)

        val mision = CatalogoEscenarios.misionDe(ZonaId.CASA)
        val resultado = MotorAuditoria.calcular(mision, situaciones, marcas)
        val guardado = repo.guardarAuditoria(mision, situaciones, marcas, resultado, reloj)

        val hallazgos = repo.hallazgosDe(guardado.auditoriaId)
        val invalido = hallazgos.first { it.situacionId == correcta.id }

        assertFalse(invalido.valido)
        assertEquals("DESCARTADO", invalido.estado)
        assertEquals(1, repo.auditoria(guardado.auditoriaId)!!.falsosPositivos)
    }

    @Test
    fun `el estado de juego se deriva de lo guardado`() = runTest {
        repo.crearPerfil("Ana", "AV_HOJA")
        jugarZona(ZonaId.CASA)

        val estado = repo.estadoJuego.first()
        val problemas = CatalogoEscenarios.deZona(ZonaId.CASA).filter { it.esProblema }

        assertEquals(1, estado.auditoriasCompletadas)
        assertEquals(problemas.size, estado.hallazgosTotales)
        assertEquals(problemas.size, estado.accionesCorrectas)
        assertEquals(1, estado.deteccionesPerfectas)
        assertEquals(100, estado.puntajeMaximo)
        assertEquals(3, estado.estrellasEn(ZonaId.CASA))
        problemas.groupBy { it.categoria }.forEach { (categoria, lista) ->
            assertEquals(lista.size, estado.hallazgosPorCategoria[categoria])
        }
    }

    @Test
    fun `repetir una mision conserva el mejor resultado y cuenta los intentos`() = runTest {
        repo.crearPerfil("Ana", "AV_HOJA")
        jugarZona(ZonaId.CASA)
        // Segunda pasada mucho peor: no debe borrar el mejor puntaje.
        jugarZona(ZonaId.CASA) { situaciones ->
            listOf(marcasPerfectas(situaciones).first())
        }

        val estado = repo.estadoJuego.first()
        assertEquals(2, estado.auditoriasCompletadas)
        assertEquals(3, estado.estrellasEn(ZonaId.CASA))
        assertEquals(100, estado.zonas.getValue(ZonaId.CASA).mejorPuntaje)
        assertEquals(2, estado.zonas.getValue(ZonaId.CASA).vecesCompletada)
    }

    @Test
    fun `sembrar las recompensas dos veces no duplica filas`() = runTest {
        repo.asegurarPerfil()
        repo.sembrarRecompensasSiHaceFalta()
        repo.sembrarRecompensasSiHaceFalta()

        assertEquals(CatalogoInsignias.insignias.size, repo.insignias.first().size)
        assertEquals(CatalogoColeccion.coleccionables.size, repo.coleccion.first().size)
    }

    @Test
    fun `las recompensas nuevas solo se anuncian una vez`() = runTest {
        repo.crearPerfil("Ana", "AV_HOJA")
        jugarZona(ZonaId.CASA)

        val estado = repo.estadoJuego.first()
        val (insigniasOtraVez, coleccionOtraVez) = repo.sincronizarRecompensas(estado)

        assertTrue(insigniasOtraVez.isEmpty())
        assertTrue(coleccionOtraVez.isEmpty())
        assertTrue(repo.coleccion.first().any { it.desbloqueado })
    }

    @Test
    fun `guardar una auditoria desbloquea coleccionables reales`() = runTest {
        repo.crearPerfil("Ana", "AV_HOJA")
        val guardadoInicial = repo.coleccion.first().count { it.desbloqueado }
        jugarZona(ZonaId.CASA)

        val desbloqueados = repo.coleccion.first().count { it.desbloqueado }
        assertEquals(0, guardadoInicial)
        assertTrue(desbloqueados > 0)
        assertTrue(repo.coleccion.first().first { it.desbloqueado }.desbloqueadoEn != null)
    }

    @Test
    fun `marcar la zona como iniciada la deja en progreso`() = runTest {
        repo.asegurarPerfil()
        repo.marcarZonaIniciada(ZonaId.PARQUE)
        repo.marcarZonaIniciada(ZonaId.PARQUE)

        val zonas = repo.progresoZonas.first()
        assertEquals(1, zonas.size)
        assertTrue(zonas.getValue(ZonaId.PARQUE).iniciada)
        assertEquals(0, zonas.getValue(ZonaId.PARQUE).estrellas)
    }

    @Test
    fun `reiniciar el progreso borra todo y conserva el perfil`() = runTest {
        repo.crearPerfil("Ana", "AV_PEZ")
        jugarZona(ZonaId.CASA)
        repo.reiniciarProgreso()

        val perfil = repo.perfil.first()!!
        val estado = repo.estadoJuego.first()

        assertEquals("Ana", perfil.alias)
        assertEquals("AV_PEZ", perfil.avatarId)
        assertEquals(0, perfil.xp)
        assertEquals(0, estado.auditoriasCompletadas)
        assertEquals(0, estado.hallazgosTotales)
        assertTrue(estado.zonas.isEmpty())
        assertTrue(repo.coleccion.first().none { it.desbloqueado })
    }

    @Test
    fun `los ajustes se guardan y no pisan el resto del perfil`() = runTest {
        repo.crearPerfil("Ana", "AV_HOJA")
        repo.actualizarAjustes(sonido = false, textoGrande = true)
        val perfil = repo.perfil.first()!!

        assertFalse(perfil.sonidoActivado)
        assertTrue(perfil.textoGrande)
        assertTrue(perfil.hapticaActivada)
        assertEquals("Ana", perfil.alias)
    }

    @Test
    fun `pedir una auditoria inexistente devuelve nulo`() = runTest {
        assertNull(repo.auditoria(9999L))
        assertTrue(repo.hallazgosDe(9999L).isEmpty())
    }
}
