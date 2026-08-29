package pe.ecoguardianes.domain.audit

import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.Medida
import pe.ecoguardianes.domain.model.Requisito
import pe.ecoguardianes.domain.model.ZonaId

/** Resumen del progreso del jugador en una zona concreta. */
data class ResumenZona(
    val zona: ZonaId,
    val estrellas: Int = 0,
    val mejorPuntaje: Int = 0,
    val vecesCompletada: Int = 0,
    val iniciada: Boolean = false
)

/**
 * Fotografía del progreso real del jugador, construida siempre a partir de lo
 * que hay guardado en la base de datos. Es la entrada de todos los cálculos de
 * recompensas y desbloqueos.
 */
data class EstadoJuego(
    val xpTotal: Int = 0,
    val hallazgosPorCategoria: Map<Categoria, Int> = emptyMap(),
    val accionesCorrectas: Int = 0,
    val auditoriasCompletadas: Int = 0,
    val deteccionesPerfectas: Int = 0,
    val puntajeMaximo: Int = 0,
    val zonas: Map<ZonaId, ResumenZona> = emptyMap(),
    val coleccionablesDesbloqueados: Int = 0
) {
    val hallazgosTotales: Int get() = hallazgosPorCategoria.values.sum()

    val zonasCompletadas: Int get() = zonas.values.count { it.estrellas >= 1 }

    val nivel: Int get() = Progresion.nivel(xpTotal)

    fun estrellasEn(zona: ZonaId): Int = zonas[zona]?.estrellas ?: 0
}

/** Traduce un [Requisito] a un valor numérico medido sobre el [EstadoJuego]. */
object Medidor {

    fun valor(estado: EstadoJuego, requisito: Requisito): Int = when (requisito.medida) {
        Medida.HALLAZGOS_CATEGORIA ->
            requisito.categoria?.let { estado.hallazgosPorCategoria[it] ?: 0 } ?: 0
        Medida.HALLAZGOS_TOTAL -> estado.hallazgosTotales
        Medida.ACCIONES_CORRECTAS -> estado.accionesCorrectas
        Medida.AUDITORIAS -> estado.auditoriasCompletadas
        Medida.ZONAS_COMPLETADAS -> estado.zonasCompletadas
        Medida.ESTRELLAS_ZONA -> requisito.zona?.let { estado.estrellasEn(it) } ?: 0
        Medida.PUNTAJE_MAXIMO -> estado.puntajeMaximo
        Medida.DETECCION_PERFECTA -> estado.deteccionesPerfectas
        Medida.XP_TOTAL -> estado.xpTotal
        Medida.COLECCIONABLES -> estado.coleccionablesDesbloqueados
    }

    fun cumplido(estado: EstadoJuego, requisito: Requisito): Boolean =
        valor(estado, requisito) >= requisito.meta

    /** Progreso 0..1 hacia el requisito, para mostrar barras honestas. */
    fun progreso(estado: EstadoJuego, requisito: Requisito): Float {
        if (requisito.meta <= 0) return 1f
        return (valor(estado, requisito).toFloat() / requisito.meta).coerceIn(0f, 1f)
    }
}
