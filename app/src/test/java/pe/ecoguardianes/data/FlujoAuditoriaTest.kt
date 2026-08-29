package pe.ecoguardianes.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import pe.ecoguardianes.data.catalogo.CatalogoAcciones
import pe.ecoguardianes.data.catalogo.CatalogoEscenarios
import pe.ecoguardianes.data.catalogo.CatalogoRetos
import pe.ecoguardianes.data.local.EcoDatabase
import pe.ecoguardianes.data.repo.EcoRepositorio
import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.Gravedad
import pe.ecoguardianes.domain.model.Situacion
import pe.ecoguardianes.domain.model.ZonaId
import pe.ecoguardianes.ui.pantallas.AuditoriaViewModel
import pe.ecoguardianes.ui.pantallas.FaseAuditoria

/** Recorre el ciclo completo de juego tal y como lo haría un niño. */
@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [33], manifest = Config.NONE)
class FlujoAuditoriaTest {

    private lateinit var db: EcoDatabase
    private lateinit var repo: EcoRepositorio
    private lateinit var vm: AuditoriaViewModel
    private val dispatcher = StandardTestDispatcher()

    @Before
    fun preparar() {
        Dispatchers.setMain(dispatcher)
        val contexto = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(contexto, EcoDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        repo = EcoRepositorio(db) { 1_700_000_000_000L }
        vm = AuditoriaViewModel(repo) { 1_700_000_000_000L }
    }

    @After
    fun limpiar() {
        db.close()
        Dispatchers.resetMain()
    }

    /**
     * Espera a que termine una corrutina del ViewModel que pasa por Room.
     * Room resuelve sus consultas en su propio executor, así que el reloj
     * virtual de `runTest` por sí solo no basta para saber que ya acabó.
     */
    private suspend fun TestScope.esperarA(intentos: Int = 300, condicion: suspend () -> Boolean) {
        repeat(intentos) {
            advanceUntilIdle()
            if (condicion()) return
            Thread.sleep(10)
        }
        advanceUntilIdle()
    }

    private fun primerProblema(): Situacion =
        CatalogoEscenarios.deZona(ZonaId.CASA).first { it.esProblema }

    private fun primeraCorrecta(): Situacion =
        CatalogoEscenarios.deZona(ZonaId.CASA).first { !it.esProblema }

    @Test
    fun `cargar la mision prepara el escenario completo`() = runTest {
        vm.cargar(ZonaId.CASA)
        advanceUntilIdle()
        val estado = vm.estado.value

        assertFalse(estado.cargando)
        assertEquals(ZonaId.CASA, estado.zona)
        assertEquals(CatalogoEscenarios.deZona(ZonaId.CASA).size, estado.situaciones.size)
        assertEquals(FaseAuditoria.EXPLORAR, estado.fase)
        assertEquals(0, estado.hallazgosRegistrados)

        esperarA { repo.progresoZonas.first().containsKey(ZonaId.CASA) }
        assertTrue(repo.progresoZonas.first().getValue(ZonaId.CASA).iniciada)
    }

    @Test
    fun `tocar dos veces el mismo objeto no duplica el hallazgo`() = runTest {
        vm.cargar(ZonaId.CASA)
        advanceUntilIdle()
        val situacion = primerProblema()

        vm.tocarSituacion(situacion.id)
        vm.registrarHallazgo()
        vm.tocarSituacion(situacion.id)
        vm.registrarHallazgo()

        assertEquals(1, vm.estado.value.hallazgosRegistrados)
    }

    @Test
    fun `identificar bien una situacion correcta la cierra sin penalizacion`() = runTest {
        vm.cargar(ZonaId.CASA)
        advanceUntilIdle()
        val correcta = primeraCorrecta()

        vm.tocarSituacion(correcta.id)
        vm.marcarComoCorrecta()
        val estado = vm.estado.value

        assertEquals(FaseAuditoria.RESUELTO, estado.fase)
        assertTrue(estado.feedback!!.acierto)
        assertTrue(correcta.id in estado.resueltas)
        assertEquals(0, estado.hallazgosRegistrados)
    }

    @Test
    fun `acusar una situacion correcta abre la revision y permite retirarla`() = runTest {
        vm.cargar(ZonaId.CASA)
        advanceUntilIdle()
        val correcta = primeraCorrecta()

        vm.tocarSituacion(correcta.id)
        vm.registrarHallazgo()
        assertEquals(FaseAuditoria.FALSO_POSITIVO, vm.estado.value.fase)
        assertEquals(1, vm.estado.value.hallazgosRegistrados)

        vm.retirarHallazgo()
        assertEquals(0, vm.estado.value.hallazgosRegistrados)
        assertEquals(0, vm.estado.value.retiradasDisponibles)
    }

    @Test
    fun `solo se puede retirar un hallazgo por mision`() = runTest {
        vm.cargar(ZonaId.CASA)
        advanceUntilIdle()
        val correctas = CatalogoEscenarios.deZona(ZonaId.CASA).filter { !it.esProblema }

        vm.tocarSituacion(correctas[0].id)
        vm.registrarHallazgo()
        vm.retirarHallazgo()

        vm.tocarSituacion(correctas[1].id)
        vm.registrarHallazgo()
        vm.retirarHallazgo()

        assertEquals(FaseAuditoria.FALSO_POSITIVO, vm.estado.value.fase)
        assertEquals(1, vm.estado.value.hallazgosRegistrados)
    }

    @Test
    fun `una clasificacion erronea deja reintentar y despues explica la respuesta`() = runTest {
        vm.cargar(ZonaId.CASA)
        advanceUntilIdle()
        val situacion = primerProblema()
        val categoriaMala = Categoria.entries.first { it != situacion.categoria }

        vm.tocarSituacion(situacion.id)
        vm.registrarHallazgo()

        vm.elegirCategoria(categoriaMala)
        vm.elegirGravedad(situacion.gravedad)
        vm.confirmarClasificacion()

        assertEquals(FaseAuditoria.CLASIFICAR, vm.estado.value.fase)
        assertFalse(vm.estado.value.feedback!!.acierto)
        assertNotNull(vm.estado.value.feedback!!.pista)

        vm.elegirCategoria(categoriaMala)
        vm.confirmarClasificacion()

        // Al segundo fallo ECO lo explica y la partida avanza.
        assertEquals(FaseAuditoria.REGLA, vm.estado.value.fase)
        assertEquals(2, vm.estado.value.intentos)
    }

    @Test
    fun `el ciclo completo de un hallazgo llega hasta resuelto`() = runTest {
        vm.cargar(ZonaId.CASA)
        advanceUntilIdle()
        val situacion = primerProblema()

        vm.tocarSituacion(situacion.id)
        vm.registrarHallazgo()
        vm.elegirCategoria(situacion.categoria)
        vm.elegirGravedad(situacion.gravedad)
        vm.confirmarClasificacion()
        assertEquals(FaseAuditoria.REGLA, vm.estado.value.fase)
        assertNotNull(vm.estado.value.regla)

        vm.continuarDesdeRegla()
        assertEquals(FaseAuditoria.ACCION, vm.estado.value.fase)

        vm.elegirAccion(CatalogoAcciones.correctaDe(situacion.id)!!.id)

        if (situacion.retoId != null) {
            assertEquals(FaseAuditoria.RETO, vm.estado.value.fase)
            val reto = CatalogoRetos.reto(situacion.retoId)!!
            reto.piezas.forEach { vm.colocarPieza(it.id, it.destinoCorrectoId) }
            vm.verificarReto()
            assertEquals(true, vm.estado.value.retoVerificado)
        }

        assertEquals(FaseAuditoria.RESUELTO, vm.estado.value.fase)
        val marca = vm.estado.value.marcas.getValue(situacion.id)
        assertTrue(marca.accionAcertada)
        assertEquals(situacion.categoria, marca.categoriaElegida)
    }

    @Test
    fun `un reto mal resuelto no avanza y explica cuanto falta`() = runTest {
        vm.cargar(ZonaId.CASA)
        advanceUntilIdle()
        val situacion = CatalogoEscenarios.deZona(ZonaId.CASA)
            .first { it.esProblema && it.retoId != null }
        val reto = CatalogoRetos.reto(situacion.retoId)!!

        vm.tocarSituacion(situacion.id)
        vm.registrarHallazgo()
        vm.elegirCategoria(situacion.categoria)
        vm.elegirGravedad(situacion.gravedad)
        vm.confirmarClasificacion()
        vm.continuarDesdeRegla()
        vm.elegirAccion(CatalogoAcciones.correctaDe(situacion.id)!!.id)

        val destinoMalo = reto.destinos.first { it.id != reto.piezas.first().destinoCorrectoId }
        reto.piezas.forEach { vm.colocarPieza(it.id, destinoMalo.id) }
        vm.verificarReto()

        assertEquals(FaseAuditoria.RETO, vm.estado.value.fase)
        assertEquals(false, vm.estado.value.retoVerificado)
        assertNotNull(vm.estado.value.feedback)

        vm.quitarPieza(reto.piezas.first().id)
        assertEquals(reto.piezas.size - 1, vm.estado.value.solucionReto.size)
    }

    @Test
    fun `no se puede cerrar la auditoria sin el minimo de hallazgos`() = runTest {
        vm.cargar(ZonaId.CASA)
        advanceUntilIdle()
        assertFalse(vm.estado.value.puedeCerrar)

        val problemas = CatalogoEscenarios.deZona(ZonaId.CASA).filter { it.esProblema }
        val minimo = CatalogoEscenarios.misionDe(ZonaId.CASA).minimoHallazgos
        problemas.take(minimo).forEach {
            vm.tocarSituacion(it.id)
            vm.registrarHallazgo()
            vm.terminarSituacion()
        }

        assertTrue(vm.estado.value.puedeCerrar)
    }

    @Test
    fun `cerrar la auditoria guarda la ficha y devuelve su identificador`() = runTest {
        repo.crearPerfil("Ana", "AV_HOJA")
        vm.cargar(ZonaId.CASA)
        advanceUntilIdle()

        CatalogoEscenarios.deZona(ZonaId.CASA).filter { it.esProblema }.forEach {
            vm.tocarSituacion(it.id)
            vm.registrarHallazgo()
            vm.elegirCategoria(it.categoria)
            vm.elegirGravedad(it.gravedad)
            vm.confirmarClasificacion()
            vm.continuarDesdeRegla()
            vm.elegirAccion(CatalogoAcciones.correctaDe(it.id)!!.id)
            if (it.retoId != null) {
                val reto = CatalogoRetos.reto(it.retoId)!!
                reto.piezas.forEach { pieza -> vm.colocarPieza(pieza.id, pieza.destinoCorrectoId) }
                vm.verificarReto()
            }
            vm.terminarSituacion()
        }

        var idDevuelto = -1L
        vm.cerrarAuditoria { idDevuelto = it }
        esperarA { idDevuelto > 0 }

        assertTrue("La auditoría no llegó a guardarse", idDevuelto > 0)
        val auditoria = repo.auditoria(idDevuelto)!!
        assertEquals(100, auditoria.puntaje)
        assertTrue(auditoria.aprobada)
        assertTrue(
            "resumen=" + vm.estado.value.resumen + " juego=" + repo.estadoJuego.first(),
            vm.estado.value.resumen!!.nuevasInsignias.isNotEmpty() ||
                vm.estado.value.resumen!!.nuevosColeccionables.isNotEmpty()
        )
    }

    @Test
    fun `reiniciar la mision limpia el acta pero mantiene la zona`() = runTest {
        vm.cargar(ZonaId.CASA)
        advanceUntilIdle()
        val situacion = primerProblema()
        vm.tocarSituacion(situacion.id)
        vm.registrarHallazgo()
        assertEquals(1, vm.estado.value.hallazgosRegistrados)

        vm.reiniciarMision()
        advanceUntilIdle()

        assertEquals(0, vm.estado.value.hallazgosRegistrados)
        assertEquals(ZonaId.CASA, vm.estado.value.zona)
        assertEquals(FaseAuditoria.EXPLORAR, vm.estado.value.fase)
    }

    @Test
    fun `las acciones ofrecidas se barajan pero siguen siendo las mismas`() = runTest {
        vm.cargar(ZonaId.CASA)
        advanceUntilIdle()
        val situacion = primerProblema()
        vm.tocarSituacion(situacion.id)

        val ofrecidas = vm.estado.value.acciones
        assertEquals(3, ofrecidas.size)
        assertEquals(
            CatalogoAcciones.accionesDe(situacion.id).map { it.id }.toSet(),
            ofrecidas.map { it.id }.toSet()
        )
    }

    @Test
    fun `una gravedad equivocada tambien cuenta como clasificacion incorrecta`() = runTest {
        vm.cargar(ZonaId.CASA)
        advanceUntilIdle()
        val situacion = primerProblema()
        val gravedadMala = Gravedad.entries.first { it != situacion.gravedad && it.esProblema }

        vm.tocarSituacion(situacion.id)
        vm.registrarHallazgo()
        vm.elegirCategoria(situacion.categoria)
        vm.elegirGravedad(gravedadMala)
        vm.confirmarClasificacion()

        assertEquals(FaseAuditoria.CLASIFICAR, vm.estado.value.fase)
        assertFalse(vm.estado.value.feedback!!.acierto)
    }
}
