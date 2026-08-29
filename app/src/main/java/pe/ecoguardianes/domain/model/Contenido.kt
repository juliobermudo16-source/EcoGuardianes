package pe.ecoguardianes.domain.model

/**
 * Referencia a una norma ambiental peruana REAL.
 *
 * Se limita al nombre oficial de la norma y a una descripción general de su
 * finalidad. No se citan artículos ni se atribuyen obligaciones concretas:
 * la aplicación es material educativo, no asesoría jurídica.
 */
data class ReferenciaNormativa(
    val norma: String,
    val finalidad: String,
    val emisor: String
)

/**
 * Una regla ambiental. Siempre tiene una versión sencilla para el niño
 * (reglaSimple) y, cuando corresponde, una referencia normativa real separada
 * y claramente identificada.
 */
data class ReglaAmbiental(
    val id: String,
    val categoria: Categoria,
    val titulo: String,
    val reglaSimple: String,
    val explicacion: String,
    val ejemplo: String,
    val accionCorrecta: String,
    val referencia: ReferenciaNormativa? = null
) {
    val tieneReferenciaReal: Boolean get() = referencia != null
}

/** Una posible acción correctiva que el jugador puede proponer. */
data class AccionCorrectiva(
    val id: String,
    val texto: String,
    val esCorrecta: Boolean,
    val categoria: Categoria,
    val explicacion: String
)

/** Tipos de mini-reto interactivo para resolver una no conformidad. */
enum class TipoReto(val etiqueta: String) {
    CLASIFICAR("Clasifica cada residuo"),
    ARRASTRAR("Coloca cada cosa en su lugar"),
    INTERRUPTOR("Apaga o cierra lo que sobra"),
    CONECTAR("Une cada problema con su solución"),
    ORDENAR("Ordena los pasos"),
    SELECCION("Elige todo lo que ayuda"),
    CHECKLIST("Completa la lista de verificación")
}

/** Pieza que el jugador manipula dentro de un mini-reto. */
data class PiezaReto(
    val id: String,
    val etiqueta: String,
    val icono: IconoAmb,
    val destinoCorrectoId: String,
    val orden: Int = 0
)

/** Destino (contenedor, casilla o respuesta) de un mini-reto. */
data class DestinoReto(
    val id: String,
    val etiqueta: String,
    val icono: IconoAmb,
    val colorHex: Long
)

/** Mini-reto interactivo asociado a una acción correctiva. */
data class Reto(
    val id: String,
    val tipo: TipoReto,
    val enunciado: String,
    val ayuda: String,
    val piezas: List<PiezaReto>,
    val destinos: List<DestinoReto>
) {
    /** Comprueba una solución propuesta con el formato pieza -> destino elegido. */
    fun esSolucionCorrecta(solucion: Map<String, String>): Boolean {
        if (piezas.isEmpty()) return false
        return piezas.all { solucion[it.id] == it.destinoCorrectoId }
    }

    fun aciertos(solucion: Map<String, String>): Int =
        piezas.count { solucion[it.id] == it.destinoCorrectoId }
}

/**
 * Una situación observable dentro de un escenario.
 * Puede ser correcta (CONFORME) o un problema que hay que detectar.
 */
data class Situacion(
    val id: String,
    val zona: ZonaId,
    val nombre: String,
    val icono: IconoAmb,
    val x: Float,
    val y: Float,
    val categoria: Categoria,
    val gravedad: Gravedad,
    val observacion: String,
    val explicacion: String,
    val pista: String,
    val reglaId: String,
    val accionesIds: List<String>,
    val retoId: String? = null,
    val xp: Int = 20,
    val nivelMinimo: Int = 1
) {
    val esProblema: Boolean get() = gravedad.esProblema
}

/** Una misión de auditoría dentro de una zona. */
data class Mision(
    val id: String,
    val zona: ZonaId,
    val titulo: String,
    val briefingEco: String,
    val objetivo: String,
    val situacionesIds: List<String>,
    val minimoHallazgos: Int,
    val nivel: Int
)

/** Métricas del progreso que sirven de requisito para las recompensas. */
enum class Medida(val etiqueta: String) {
    HALLAZGOS_CATEGORIA("hallazgos de la categoría"),
    HALLAZGOS_TOTAL("hallazgos registrados"),
    ACCIONES_CORRECTAS("acciones correctivas acertadas"),
    AUDITORIAS("auditorías completadas"),
    ZONAS_COMPLETADAS("zonas completadas"),
    ESTRELLAS_ZONA("estrellas en la zona"),
    PUNTAJE_MAXIMO("puntaje máximo alcanzado"),
    DETECCION_PERFECTA("auditorías con detección perfecta"),
    XP_TOTAL("puntos de experiencia"),
    COLECCIONABLES("elementos de la colección")
}

/** Requisito medible y verificable para desbloquear una recompensa. */
data class Requisito(
    val medida: Medida,
    val meta: Int,
    val categoria: Categoria? = null,
    val zona: ZonaId? = null
) {
    val descripcion: String
        get() = when (medida) {
            Medida.HALLAZGOS_CATEGORIA ->
                "Registra " + meta + " hallazgos de " + (categoria?.etiqueta ?: "cualquier tipo")
            Medida.HALLAZGOS_TOTAL -> "Registra " + meta + " hallazgos en total"
            Medida.ACCIONES_CORRECTAS -> "Acierta " + meta + " acciones correctivas"
            Medida.AUDITORIAS -> "Completa " + meta + " auditorías"
            Medida.ZONAS_COMPLETADAS -> "Completa " + meta + " zonas del mapa"
            Medida.ESTRELLAS_ZONA ->
                "Consigue " + meta + " estrellas en " + (zona?.titulo ?: "una zona")
            Medida.PUNTAJE_MAXIMO -> "Alcanza " + meta + "% en una auditoría"
            Medida.DETECCION_PERFECTA ->
                "Detecta el 100% de los problemas en " + meta + " auditoría(s)"
            Medida.XP_TOTAL -> "Acumula " + meta + " XP"
            Medida.COLECCIONABLES -> "Reúne " + meta + " elementos de la colección"
        }
}

/** Insignia otorgada por logros reales. */
data class Insignia(
    val id: String,
    val nombre: String,
    val simbolo: String,
    val icono: IconoAmb,
    val colorHex: Long,
    val descripcion: String,
    val requisito: Requisito
)

enum class TipoColeccionable(val etiqueta: String, val simbolo: String) {
    FAUNA("Fauna", "🐾"),
    FLORA("Flora", "🌿"),
    RECICLABLE("Reciclables", "♻️"),
    HERRAMIENTA("Herramientas", "🧰"),
    DESCUBRIMIENTO("Descubrimientos", "🔎")
}

/** Elemento de la colección ambiental. */
data class Coleccionable(
    val id: String,
    val nombre: String,
    val tipo: TipoColeccionable,
    val icono: IconoAmb,
    val colorHex: Long,
    val descripcion: String,
    val datoCurioso: String,
    val requisito: Requisito
)

/** Avatar local del perfil. No contiene ningún dato personal. */
data class Avatar(
    val id: String,
    val nombre: String,
    val colorHex: Long,
    val colorSecundarioHex: Long,
    val accesorio: IconoAmb
)
