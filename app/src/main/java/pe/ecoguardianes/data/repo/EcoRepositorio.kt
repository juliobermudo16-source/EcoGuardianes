package pe.ecoguardianes.data.repo

import androidx.room.withTransaction
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pe.ecoguardianes.data.catalogo.CatalogoAcciones
import pe.ecoguardianes.data.catalogo.CatalogoColeccion
import pe.ecoguardianes.data.catalogo.CatalogoEscenarios
import pe.ecoguardianes.data.catalogo.CatalogoInsignias
import pe.ecoguardianes.data.local.AuditoriaEntity
import pe.ecoguardianes.data.local.ColeccionableEntity
import pe.ecoguardianes.data.local.EcoDatabase
import pe.ecoguardianes.data.local.HallazgoEntity
import pe.ecoguardianes.data.local.InsigniaEntity
import pe.ecoguardianes.data.local.PerfilEntity
import pe.ecoguardianes.data.local.ProgresoZonaEntity
import pe.ecoguardianes.domain.audit.EstadoJuego
import pe.ecoguardianes.domain.audit.EvaluadorRecompensas
import pe.ecoguardianes.domain.audit.MarcaJugador
import pe.ecoguardianes.domain.audit.Progresion
import pe.ecoguardianes.domain.audit.ResultadoAuditoria
import pe.ecoguardianes.domain.audit.ResumenZona
import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.EstadoHallazgo
import pe.ecoguardianes.domain.model.Mision
import pe.ecoguardianes.domain.model.Situacion
import pe.ecoguardianes.domain.model.ZonaId

/** Lo que ocurrió al guardar una auditoría, para poder celebrarlo en pantalla. */
data class ResumenGuardado(
    val auditoriaId: Long,
    val xpAnterior: Int,
    val xpNuevo: Int,
    val subioDeNivel: Boolean,
    val cambioDeRango: Boolean,
    val nuevasInsignias: List<String>,
    val nuevosColeccionables: List<String>
)

/**
 * Punto único de acceso al progreso del jugador.
 *
 * Combina el catálogo educativo (contenido fijo) con la base de datos Room
 * (todo lo que el jugador consigue). Ningún dato sale del dispositivo.
 */
class EcoRepositorio(
    private val db: EcoDatabase,
    private val ahora: () -> Long = { System.currentTimeMillis() }
) {

    private val perfilDao = db.perfilDao()
    private val zonaDao = db.progresoZonaDao()
    private val auditoriaDao = db.auditoriaDao()
    private val hallazgoDao = db.hallazgoDao()
    private val insigniaDao = db.insigniaDao()
    private val coleccionableDao = db.coleccionableDao()

    // ------------------------------------------------------------------ Perfil

    val perfil: Flow<PerfilEntity?> = perfilDao.observar()

    suspend fun asegurarPerfil(): PerfilEntity {
        val existente = perfilDao.obtener()
        if (existente != null) return existente
        val nuevo = PerfilEntity(creadoEn = ahora())
        perfilDao.guardar(nuevo)
        sembrarRecompensasSiHaceFalta()
        return nuevo
    }

    suspend fun crearPerfil(alias: String, avatarId: String) {
        val limpio = alias.trim().take(MAX_ALIAS).ifBlank { ALIAS_POR_DEFECTO }
        val actual = perfilDao.obtener() ?: PerfilEntity(creadoEn = ahora())
        perfilDao.guardar(
            actual.copy(
                alias = limpio,
                avatarId = avatarId,
                onboardingCompletado = true,
                creadoEn = if (actual.creadoEn == 0L) ahora() else actual.creadoEn
            )
        )
        sembrarRecompensasSiHaceFalta()
    }

    suspend fun actualizarAjustes(
        sonido: Boolean? = null,
        haptica: Boolean? = null,
        textoGrande: Boolean? = null,
        pistas: Boolean? = null,
        alias: String? = null,
        avatarId: String? = null
    ) {
        val actual = perfilDao.obtener() ?: asegurarPerfil()
        perfilDao.guardar(
            actual.copy(
                sonidoActivado = sonido ?: actual.sonidoActivado,
                hapticaActivada = haptica ?: actual.hapticaActivada,
                textoGrande = textoGrande ?: actual.textoGrande,
                pistasAutomaticas = pistas ?: actual.pistasAutomaticas,
                alias = alias?.trim()?.take(MAX_ALIAS)?.ifBlank { actual.alias } ?: actual.alias,
                avatarId = avatarId ?: actual.avatarId
            )
        )
    }

    // ------------------------------------------------------------ Estado juego

    val estadoJuego: Flow<EstadoJuego> = run {
        val base = combine(
            perfilDao.observar(),
            zonaDao.observarTodas()
        ) { perfil, zonas ->
            (perfil?.xp ?: 0) to zonas.mapNotNull { z ->
                ZonaId.porId(z.zonaId)?.let { zona ->
                    zona to ResumenZona(
                        zona = zona,
                        estrellas = z.estrellas,
                        mejorPuntaje = z.mejorPuntaje,
                        vecesCompletada = z.vecesCompletada,
                        iniciada = z.iniciada
                    )
                }
            }.toMap()
        }

        val metricas = combine(
            hallazgoDao.conteoPorCategoria(),
            hallazgoDao.contarAccionesCorrectas(),
            auditoriaDao.contarCompletadas(),
            auditoriaDao.contarDeteccionPerfecta(),
            auditoriaDao.puntajeMaximo()
        ) { conteos, acciones, auditorias, perfectas, maximo ->
            Metricas(
                porCategoria = conteos.mapNotNull { c ->
                    Categoria.porId(c.categoria)?.let { it to c.total }
                }.toMap(),
                accionesCorrectas = acciones,
                auditorias = auditorias,
                perfectas = perfectas,
                puntajeMaximo = maximo
            )
        }

        combine(
            base,
            metricas,
            coleccionableDao.contarDesbloqueados()
        ) { (xp, zonas), m, coleccion ->
            EstadoJuego(
                xpTotal = xp,
                hallazgosPorCategoria = m.porCategoria,
                accionesCorrectas = m.accionesCorrectas,
                auditoriasCompletadas = m.auditorias,
                deteccionesPerfectas = m.perfectas,
                puntajeMaximo = m.puntajeMaximo,
                zonas = zonas,
                coleccionablesDesbloqueados = coleccion
            )
        }
    }

    private data class Metricas(
        val porCategoria: Map<Categoria, Int>,
        val accionesCorrectas: Int,
        val auditorias: Int,
        val perfectas: Int,
        val puntajeMaximo: Int
    )

    // ------------------------------------------------------------ Recompensas

    val insignias: Flow<List<InsigniaEntity>> = insigniaDao.observarTodas()
    val coleccion: Flow<List<ColeccionableEntity>> = coleccionableDao.observarTodos()
    val auditorias: Flow<List<AuditoriaEntity>> = auditoriaDao.observarTodas()

    val progresoZonas: Flow<Map<ZonaId, ResumenZona>> = zonaDao.observarTodas().map { lista ->
        lista.mapNotNull { z ->
            ZonaId.porId(z.zonaId)?.let { zona ->
                zona to ResumenZona(
                    zona = zona,
                    estrellas = z.estrellas,
                    mejorPuntaje = z.mejorPuntaje,
                    vecesCompletada = z.vecesCompletada,
                    iniciada = z.iniciada
                )
            }
        }.toMap()
    }

    /** Crea las filas de insignias y coleccionables la primera vez. */
    suspend fun sembrarRecompensasSiHaceFalta() {
        val existentes = insigniaDao.observarTodas().first()
        if (existentes.size != CatalogoInsignias.insignias.size) {
            insigniaDao.guardarTodas(
                CatalogoInsignias.insignias.map { insignia ->
                    existentes.firstOrNull { it.id == insignia.id }
                        ?: InsigniaEntity(id = insignia.id, meta = insignia.requisito.meta)
                }
            )
        }
        val colExistentes = coleccionableDao.observarTodos().first()
        if (colExistentes.size != CatalogoColeccion.coleccionables.size) {
            coleccionableDao.guardarTodos(
                CatalogoColeccion.coleccionables.map { c ->
                    colExistentes.firstOrNull { it.id == c.id }
                        ?: ColeccionableEntity(id = c.id, meta = c.requisito.meta)
                }
            )
        }
    }

    /** Recalcula el estado de todas las recompensas y devuelve las nuevas. */
    suspend fun sincronizarRecompensas(estado: EstadoJuego): Pair<List<String>, List<String>> {
        sembrarRecompensasSiHaceFalta()

        val insigniasGuardadas = insigniaDao.observarTodas().first()
        val yaInsignias = insigniasGuardadas.filter { it.desbloqueada }.map { it.id }.toSet()
        val progresoInsignias =
            EvaluadorRecompensas.evaluarInsignias(estado, CatalogoInsignias.insignias)
        val nuevasInsignias =
            EvaluadorRecompensas.nuevosDesbloqueos(progresoInsignias, yaInsignias)

        insigniaDao.guardarTodas(
            progresoInsignias.map { p ->
                val previa = insigniasGuardadas.firstOrNull { it.id == p.id }
                InsigniaEntity(
                    id = p.id,
                    desbloqueada = p.desbloqueada || previa?.desbloqueada == true,
                    desbloqueadaEn = previa?.desbloqueadaEn
                        ?: if (p.desbloqueada) ahora() else null,
                    progresoActual = p.actual,
                    meta = p.meta
                )
            }
        )

        val colGuardados = coleccionableDao.observarTodos().first()
        val yaColeccion = colGuardados.filter { it.desbloqueado }.map { it.id }.toSet()
        val progresoColeccion =
            EvaluadorRecompensas.evaluarColeccionables(estado, CatalogoColeccion.coleccionables)
        val nuevosColeccionables =
            EvaluadorRecompensas.nuevosDesbloqueos(progresoColeccion, yaColeccion)

        coleccionableDao.guardarTodos(
            progresoColeccion.map { p ->
                val previo = colGuardados.firstOrNull { it.id == p.id }
                ColeccionableEntity(
                    id = p.id,
                    desbloqueado = p.desbloqueada || previo?.desbloqueado == true,
                    desbloqueadoEn = previo?.desbloqueadoEn
                        ?: if (p.desbloqueada) ahora() else null,
                    progresoActual = p.actual,
                    meta = p.meta
                )
            }
        )

        return nuevasInsignias to nuevosColeccionables
    }

    // -------------------------------------------------------------- Auditoría

    suspend fun marcarZonaIniciada(zona: ZonaId) {
        val actual = zonaDao.obtener(zona.name)
        zonaDao.guardar(
            (actual ?: ProgresoZonaEntity(zonaId = zona.name)).copy(
                iniciada = true,
                actualizadoEn = ahora()
            )
        )
    }

    /**
     * Guarda una auditoría completa: ficha, hallazgos, progreso de zona y XP.
     * Todo dentro de una transacción para que nunca quede a medias.
     */
    suspend fun guardarAuditoria(
        mision: Mision,
        situaciones: List<Situacion>,
        marcas: List<MarcaJugador>,
        resultado: ResultadoAuditoria,
        iniciadaEn: Long
    ): ResumenGuardado {
        val perfilPrevio = asegurarPerfil()
        val xpAnterior = perfilPrevio.xp
        val instante = ahora()

        val auditoriaId = db.withTransaction {
            val id = auditoriaDao.insertar(
                AuditoriaEntity(
                    misionId = mision.id,
                    zonaId = mision.zona.name,
                    iniciadaEn = iniciadaEn,
                    finalizadaEn = instante,
                    totalSituaciones = resultado.totalSituaciones,
                    problemasTotales = resultado.problemasTotales,
                    detectadosCorrectos = resultado.detectadosCorrectos,
                    falsosPositivos = resultado.falsosPositivos,
                    omitidos = resultado.omitidos,
                    conformes = resultado.conformes,
                    observaciones = resultado.observaciones,
                    noConformidades = resultado.noConformidades,
                    clasificacionesCorrectas = resultado.clasificacionesCorrectas,
                    accionesCorrectas = resultado.accionesCorrectas,
                    accionesPropuestas = resultado.accionesPropuestas,
                    puntaje = resultado.puntaje,
                    estrellas = resultado.estrellas,
                    xpGanado = resultado.xpGanado,
                    aprobada = resultado.aprobada,
                    deteccionPerfecta = resultado.deteccionPerfecta,
                    completada = true
                )
            )

            val marcasPorId = marcas.associateBy { it.situacionId }
            val filas = situaciones.mapNotNull { situacion ->
                val marca = marcasPorId[situacion.id] ?: return@mapNotNull null
                if (!marca.marcadaComoProblema) return@mapNotNull null
                val evaluacion = resultado.evaluaciones.first { it.situacionId == situacion.id }
                HallazgoEntity(
                    auditoriaId = id,
                    situacionId = situacion.id,
                    zonaId = situacion.zona.name,
                    nombre = situacion.nombre,
                    categoria = situacion.categoria.name,
                    gravedad = situacion.gravedad.name,
                    categoriaElegida = marca.categoriaElegida?.name,
                    gravedadElegida = marca.gravedadElegida?.name,
                    descripcion = situacion.observacion,
                    reglaId = situacion.reglaId,
                    accionId = marca.accionElegidaId,
                    accionCorrecta = evaluacion.accionCorrecta,
                    retoSuperado = marca.retoSuperado,
                    intentos = marca.intentos,
                    valido = evaluacion.esHallazgoValido,
                    estado = estadoDe(marca, evaluacion.esHallazgoValido).name,
                    registradoEn = instante
                )
            }
            if (filas.isNotEmpty()) hallazgoDao.insertarTodos(filas)

            val previa = zonaDao.obtener(mision.zona.name)
            zonaDao.guardar(
                ProgresoZonaEntity(
                    zonaId = mision.zona.name,
                    estrellas = maxOf(previa?.estrellas ?: 0, resultado.estrellas),
                    mejorPuntaje = maxOf(previa?.mejorPuntaje ?: 0, resultado.puntaje),
                    vecesCompletada = (previa?.vecesCompletada ?: 0) + 1,
                    iniciada = true,
                    actualizadoEn = instante
                )
            )

            perfilDao.sumarXp(resultado.xpGanado)
            id
        }

        val estado = estadoJuego.first()
        val (nuevasInsignias, nuevosColeccionables) = sincronizarRecompensas(estado)
        val xpNuevo = xpAnterior + resultado.xpGanado

        return ResumenGuardado(
            auditoriaId = auditoriaId,
            xpAnterior = xpAnterior,
            xpNuevo = xpNuevo,
            subioDeNivel = Progresion.subioDeNivel(xpAnterior, xpNuevo),
            cambioDeRango = Progresion.cambioDeRango(xpAnterior, xpNuevo),
            nuevasInsignias = nuevasInsignias,
            nuevosColeccionables = nuevosColeccionables
        )
    }

    private fun estadoDe(marca: MarcaJugador, valido: Boolean): EstadoHallazgo = when {
        !valido -> EstadoHallazgo.DESCARTADO
        marca.retoSuperado || marca.accionAcertada -> EstadoHallazgo.RESUELTO
        marca.categoriaElegida != null -> EstadoHallazgo.CLASIFICADO
        else -> EstadoHallazgo.DETECTADO
    }

    suspend fun hallazgosDe(auditoriaId: Long): List<HallazgoEntity> =
        hallazgoDao.deAuditoria(auditoriaId)

    suspend fun auditoria(id: Long): AuditoriaEntity? = auditoriaDao.obtener(id)

    /** Reinicia todo el progreso local sin tocar el contenido educativo. */
    suspend fun reiniciarProgreso() {
        db.withTransaction {
            hallazgoDao.borrarTodo()
            auditoriaDao.borrarTodo()
            zonaDao.borrarTodo()
            insigniaDao.borrarTodo()
            coleccionableDao.borrarTodo()
            val perfil = perfilDao.obtener()
            if (perfil != null) perfilDao.guardar(perfil.copy(xp = 0))
        }
        sembrarRecompensasSiHaceFalta()
    }

    companion object {
        const val MAX_ALIAS = 16
        const val ALIAS_POR_DEFECTO = "Guardián"

        /** Situaciones de una zona, listas para jugar. */
        fun situacionesDe(zona: ZonaId): List<Situacion> = CatalogoEscenarios.deZona(zona)

        /** Acciones correctivas barajadas de forma estable para una situación. */
        fun accionesBarajadas(situacionId: String) =
            CatalogoAcciones.accionesDe(situacionId)
                .shuffled(java.util.Random(situacionId.hashCode().toLong()))
    }
}
