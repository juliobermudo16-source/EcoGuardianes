package pe.ecoguardianes.domain.audit

import pe.ecoguardianes.domain.model.Rango

/** Cálculo de nivel y rango a partir de la experiencia acumulada. */
object Progresion {

    const val XP_POR_NIVEL = 250
    const val NIVEL_MAXIMO = 30

    /** Nivel numérico, empezando en 1. */
    fun nivel(xp: Int): Int {
        if (xp <= 0) return 1
        return (xp / XP_POR_NIVEL + 1).coerceAtMost(NIVEL_MAXIMO)
    }

    fun rango(xp: Int): Rango = Rango.porXp(xp.coerceAtLeast(0))

    /** XP acumulado dentro del nivel actual. */
    fun xpEnNivel(xp: Int): Int {
        if (xp <= 0) return 0
        if (nivel(xp) >= NIVEL_MAXIMO) return XP_POR_NIVEL
        return xp % XP_POR_NIVEL
    }

    /** XP que falta para subir de nivel. 0 si ya se alcanzó el nivel máximo. */
    fun xpParaSiguienteNivel(xp: Int): Int {
        if (nivel(xp) >= NIVEL_MAXIMO) return 0
        return XP_POR_NIVEL - xpEnNivel(xp)
    }

    /** Progreso 0..1 dentro del nivel actual, listo para una barra de progreso. */
    fun progresoNivel(xp: Int): Float {
        if (nivel(xp) >= NIVEL_MAXIMO) return 1f
        return (xpEnNivel(xp).toFloat() / XP_POR_NIVEL).coerceIn(0f, 1f)
    }

    /** True si al pasar de [xpAnterior] a [xpNuevo] el jugador subió de nivel. */
    fun subioDeNivel(xpAnterior: Int, xpNuevo: Int): Boolean =
        nivel(xpNuevo) > nivel(xpAnterior)

    /** True si al pasar de [xpAnterior] a [xpNuevo] el jugador cambió de rango. */
    fun cambioDeRango(xpAnterior: Int, xpNuevo: Int): Boolean =
        rango(xpNuevo) != rango(xpAnterior)
}
