package pe.ecoguardianes.domain.model

/** Las seis zonas del mapa de EcoGuardianes. */
enum class ZonaId(
    val titulo: String,
    val lema: String,
    val orden: Int,
    val xpRequerido: Int,
    val nivelRecomendado: Int
) {
    CASA("Casa", "Todo empieza en el hogar", 1, 0, 1),
    ESCUELA("Escuela", "Aprender también es cuidar", 2, 120, 1),
    PARQUE("Parque", "El pulmón del barrio", 3, 320, 2),
    RIO("Río", "El agua que nos da vida", 4, 600, 2),
    CIUDAD("Ciudad", "Miles de personas, un solo aire", 5, 950, 3),
    ZONA_INDUSTRIAL("Zona industrial", "Producir sin destruir", 6, 1400, 3);

    companion object {
        fun porId(id: String): ZonaId? = entries.firstOrNull { it.name == id }
        val enOrden: List<ZonaId> get() = entries.sortedBy { it.orden }
    }
}

/** Estado de una zona en el mapa. Nunca se representa solo con color. */
enum class EstadoZona(val etiqueta: String, val simbolo: String) {
    BLOQUEADA("Bloqueada", "🔒"),
    DISPONIBLE("Disponible", "▶"),
    EN_PROGRESO("En progreso", "⏳"),
    COMPLETADA("Completada", "★"),
    DOMINADA("Dominada", "🏆");

    val esJugable: Boolean get() = this != BLOQUEADA
}

/** Las tres grandes etapas de progresión del jugador. */
enum class Rango(
    val titulo: String,
    val simbolo: String,
    val xpMinimo: Int,
    val descripcion: String
) {
    EXPLORADOR("Explorador", "🌱", 0, "Aprende a observar con atención."),
    DETECTIVE("Detective", "🔍", 600, "Encuentra y clasifica problemas ambientales."),
    ECOGUARDIAN("EcoGuardián", "🛡️", 1600, "Analiza, relaciona reglas y propone soluciones.");

    companion object {
        fun porXp(xp: Int): Rango =
            entries.sortedByDescending { it.xpMinimo }.first { xp >= it.xpMinimo }
    }
}
