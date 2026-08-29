package pe.ecoguardianes.domain.audit

import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.Gravedad
import pe.ecoguardianes.domain.model.Mision
import pe.ecoguardianes.domain.model.Situacion
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

/**
 * Lo que el jugador afirma sobre una situación del escenario.
 *
 * `marcadaComoProblema` es la decisión de la fase de detección; el resto se va
 * completando conforme el jugador clasifica el hallazgo y propone una acción.
 */
data class MarcaJugador(
    val situacionId: String,
    val marcadaComoProblema: Boolean,
    val categoriaElegida: Categoria? = null,
    val gravedadElegida: Gravedad? = null,
    val accionElegidaId: String? = null,
    val accionAcertada: Boolean = false,
    val retoSuperado: Boolean = false,
    val intentos: Int = 0
)

/** Resultado de contrastar una marca del jugador con la situación real. */
data class EvaluacionHallazgo(
    val situacionId: String,
    val deteccionCorrecta: Boolean,
    val esFalsoPositivo: Boolean,
    val esOmision: Boolean,
    val categoriaCorrecta: Boolean,
    val gravedadCorrecta: Boolean,
    val accionCorrecta: Boolean,
    val xpObtenido: Int
) {
    /** Un hallazgo válido es un problema real que el jugador marcó como problema. */
    val esHallazgoValido: Boolean get() = deteccionCorrecta
}

/** Ficha de auditoría calculada a partir de datos reales de la partida. */
data class ResultadoAuditoria(
    val misionId: String,
    val totalSituaciones: Int,
    val problemasTotales: Int,
    val conformesTotales: Int,
    val detectadosCorrectos: Int,
    val falsosPositivos: Int,
    val omitidos: Int,
    val conformes: Int,
    val observaciones: Int,
    val noConformidades: Int,
    val clasificacionesCorrectas: Int,
    val accionesCorrectas: Int,
    val accionesPropuestas: Int,
    val puntaje: Int,
    val estrellas: Int,
    val xpGanado: Int,
    val aprobada: Boolean,
    val deteccionPerfecta: Boolean,
    val evaluaciones: List<EvaluacionHallazgo>
) {
    val porcentajeDeteccion: Float
        get() = if (problemasTotales == 0) 1f else detectadosCorrectos.toFloat() / problemasTotales
}

/**
 * Motor de auditoría ambiental.
 *
 * Es Kotlin puro: no depende de Android ni de la interfaz, de modo que todas
 * las reglas de puntuación pueden probarse con tests unitarios.
 *
 * Ponderación del puntaje (suma 100):
 *  - 40 puntos: detección de los problemas reales.
 *  - 25 puntos: clasificación correcta (categoría y gravedad) de lo detectado.
 *  - 30 puntos: acciones correctivas acertadas sobre el total de problemas.
 *  - 5 puntos: precisión, por no marcar como problema lo que sí estaba bien.
 *  Penalización: 5 puntos por cada falso positivo, hasta un máximo de 15.
 */
object MotorAuditoria {

    const val PESO_DETECCION = 40.0
    const val PESO_CLASIFICACION = 25.0
    const val PESO_CORRECCION = 30.0
    const val PESO_PRECISION = 5.0
    const val PENALIZACION_FALSO_POSITIVO = 5.0
    const val PENALIZACION_MAXIMA = 15.0

    const val UMBRAL_TRES_ESTRELLAS = 90
    const val UMBRAL_DOS_ESTRELLAS = 70
    const val UMBRAL_UNA_ESTRELLA = 50

    const val XP_ACCION_CORRECTA = 15
    const val XP_POR_ESTRELLA = 25
    const val XP_CLASIFICACION = 5

    /** Contrasta una marca concreta contra la situación real del escenario. */
    fun evaluarHallazgo(situacion: Situacion, marca: MarcaJugador?): EvaluacionHallazgo {
        val marcada = marca?.marcadaComoProblema == true
        val deteccionCorrecta = situacion.esProblema && marcada
        val esFalsoPositivo = !situacion.esProblema && marcada
        val esOmision = situacion.esProblema && !marcada

        val categoriaCorrecta =
            deteccionCorrecta && marca?.categoriaElegida == situacion.categoria
        val gravedadCorrecta =
            deteccionCorrecta && marca?.gravedadElegida == situacion.gravedad
        val accionCorrecta = deteccionCorrecta && marca?.accionAcertada == true

        var xp = 0
        if (deteccionCorrecta) xp += situacion.xp
        if (categoriaCorrecta) xp += XP_CLASIFICACION
        if (gravedadCorrecta) xp += XP_CLASIFICACION
        if (accionCorrecta) xp += XP_ACCION_CORRECTA

        return EvaluacionHallazgo(
            situacionId = situacion.id,
            deteccionCorrecta = deteccionCorrecta,
            esFalsoPositivo = esFalsoPositivo,
            esOmision = esOmision,
            categoriaCorrecta = categoriaCorrecta,
            gravedadCorrecta = gravedadCorrecta,
            accionCorrecta = accionCorrecta,
            xpObtenido = xp
        )
    }

    /** Calcula la ficha completa de auditoría de una misión. */
    fun calcular(
        mision: Mision,
        situaciones: List<Situacion>,
        marcas: List<MarcaJugador>
    ): ResultadoAuditoria {
        val marcasPorId = marcas.associateBy { it.situacionId }
        val evaluaciones = situaciones.map { evaluarHallazgo(it, marcasPorId[it.id]) }

        val problemasTotales = situaciones.count { it.esProblema }
        val conformesTotales = situaciones.size - problemasTotales
        val detectados = evaluaciones.count { it.deteccionCorrecta }
        val falsosPositivos = evaluaciones.count { it.esFalsoPositivo }
        val omitidos = evaluaciones.count { it.esOmision }

        val detectadasSituaciones = situaciones.filter { s ->
            evaluaciones.first { it.situacionId == s.id }.deteccionCorrecta
        }
        val noConformidades = detectadasSituaciones.count { it.gravedad == Gravedad.NO_CONFORMIDAD }
        val observaciones = detectadasSituaciones.count { it.gravedad == Gravedad.OBSERVACION }
        val conformesReconocidos = conformesTotales - falsosPositivos

        val clasificacionesCorrectas =
            evaluaciones.count { it.categoriaCorrecta && it.gravedadCorrecta }
        val accionesCorrectas = evaluaciones.count { it.accionCorrecta }
        val accionesPropuestas = marcas.count { it.accionElegidaId != null }

        val ratioDeteccion =
            if (problemasTotales == 0) 1.0 else detectados.toDouble() / problemasTotales
        val ratioClasificacion = when {
            problemasTotales == 0 -> 1.0
            detectados == 0 -> 0.0
            else -> clasificacionesCorrectas.toDouble() / detectados
        }
        val ratioCorreccion =
            if (problemasTotales == 0) 1.0 else accionesCorrectas.toDouble() / problemasTotales
        val ratioPrecision =
            if (conformesTotales == 0) 1.0 else conformesReconocidos.toDouble() / conformesTotales

        val base = PESO_DETECCION * ratioDeteccion +
            PESO_CLASIFICACION * ratioClasificacion +
            PESO_CORRECCION * ratioCorreccion +
            PESO_PRECISION * ratioPrecision
        val penalizacion =
            min(PENALIZACION_MAXIMA, PENALIZACION_FALSO_POSITIVO * falsosPositivos)

        val puntaje = (base - penalizacion).roundToInt().coerceIn(0, 100)
        val estrellas = estrellasPara(puntaje)

        val xpHallazgos = evaluaciones.sumOf { it.xpObtenido }
        val xpGanado = max(0, xpHallazgos + estrellas * XP_POR_ESTRELLA)

        return ResultadoAuditoria(
            misionId = mision.id,
            totalSituaciones = situaciones.size,
            problemasTotales = problemasTotales,
            conformesTotales = conformesTotales,
            detectadosCorrectos = detectados,
            falsosPositivos = falsosPositivos,
            omitidos = omitidos,
            conformes = max(0, conformesReconocidos),
            observaciones = observaciones,
            noConformidades = noConformidades,
            clasificacionesCorrectas = clasificacionesCorrectas,
            accionesCorrectas = accionesCorrectas,
            accionesPropuestas = accionesPropuestas,
            puntaje = puntaje,
            estrellas = estrellas,
            xpGanado = xpGanado,
            aprobada = detectados >= mision.minimoHallazgos && puntaje >= UMBRAL_UNA_ESTRELLA,
            deteccionPerfecta = problemasTotales > 0 &&
                detectados == problemasTotales &&
                falsosPositivos == 0,
            evaluaciones = evaluaciones
        )
    }

    fun estrellasPara(puntaje: Int): Int = when {
        puntaje >= UMBRAL_TRES_ESTRELLAS -> 3
        puntaje >= UMBRAL_DOS_ESTRELLAS -> 2
        puntaje >= UMBRAL_UNA_ESTRELLA -> 1
        else -> 0
    }
}
