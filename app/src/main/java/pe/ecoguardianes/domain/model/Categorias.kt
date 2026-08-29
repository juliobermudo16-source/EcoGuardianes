package pe.ecoguardianes.domain.model

/**
 * Categorías educativas de no conformidad ambiental.
 * Cada categoría tiene identidad propia (icono + color) para que el niño
 * no dependa únicamente del color para reconocerla.
 */
enum class Categoria(
    val etiqueta: String,
    val simbolo: String,
    val colorHex: Long,
    val explicacion: String
) {
    RESIDUOS(
        etiqueta = "Residuos",
        simbolo = "♻️",
        colorHex = 0xFF2E9E5B,
        explicacion = "Todo lo que ya no usamos debe separarse y colocarse en el recipiente correcto."
    ),
    AGUA(
        etiqueta = "Agua",
        simbolo = "💧",
        colorHex = 0xFF1E88C7,
        explicacion = "El agua limpia es limitada: hay que cuidarla y no ensuciarla."
    ),
    AIRE(
        etiqueta = "Aire",
        simbolo = "🌬️",
        colorHex = 0xFF6FB3D9,
        explicacion = "Respiramos aire todos los días; el humo y el polvo lo ensucian."
    ),
    RUIDO(
        etiqueta = "Ruido",
        simbolo = "🔊",
        colorHex = 0xFF8E5FD9,
        explicacion = "El ruido muy fuerte molesta a las personas y también a los animales."
    ),
    AREAS_VERDES(
        etiqueta = "Áreas verdes",
        simbolo = "🌳",
        colorHex = 0xFF4CAF50,
        explicacion = "Los parques y jardines nos dan sombra, aire limpio y espacio para jugar."
    ),
    BIODIVERSIDAD(
        etiqueta = "Biodiversidad",
        simbolo = "🐾",
        colorHex = 0xFFE08A2E,
        explicacion = "Plantas y animales forman la vida del planeta; cada especie cumple un papel."
    ),
    ENERGIA(
        etiqueta = "Energía",
        simbolo = "⚡",
        colorHex = 0xFFF2B705,
        explicacion = "La energía que no usamos bien se desperdicia y contamina."
    ),
    CONTAMINACION(
        etiqueta = "Contaminación",
        simbolo = "🌎",
        colorHex = 0xFFD1495B,
        explicacion = "Sustancias o materiales fuera de lugar que dañan el ambiente."
    );

    companion object {
        fun porId(id: String): Categoria? = entries.firstOrNull { it.name == id }
    }
}

/**
 * Gravedad educativa de una situación observada durante la auditoría.
 * Se acompaña siempre de símbolo y etiqueta para no depender del color.
 */
enum class Gravedad(
    val etiqueta: String,
    val simbolo: String,
    val colorHex: Long,
    val descripcion: String
) {
    CONFORME(
        etiqueta = "Situación correcta",
        simbolo = "✓",
        colorHex = 0xFF2E9E5B,
        descripcion = "Aquí se está cumpliendo la regla ambiental."
    ),
    OBSERVACION(
        etiqueta = "Aspecto por mejorar",
        simbolo = "!",
        colorHex = 0xFFE8A020,
        descripcion = "Todavía no es un problema grave, pero conviene mejorarlo."
    ),
    NO_CONFORMIDAD(
        etiqueta = "No conformidad",
        simbolo = "✕",
        colorHex = 0xFFD1495B,
        descripcion = "Una situación que no cumple una regla ambiental y puede causar un problema."
    );

    val esProblema: Boolean get() = this != CONFORME
}

/** Estado del hallazgo dentro de la ficha de auditoría. */
enum class EstadoHallazgo(val etiqueta: String) {
    DETECTADO("Detectado"),
    CLASIFICADO("Clasificado"),
    RESUELTO("Resuelto"),
    DESCARTADO("Descartado")
}
