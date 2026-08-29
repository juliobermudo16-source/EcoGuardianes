package pe.ecoguardianes.domain.audit

import pe.ecoguardianes.domain.model.Coleccionable
import pe.ecoguardianes.domain.model.EstadoZona
import pe.ecoguardianes.domain.model.Insignia
import pe.ecoguardianes.domain.model.Medida
import pe.ecoguardianes.domain.model.ZonaId

/** Progreso hacia una recompensa concreta (insignia o coleccionable). */
data class ProgresoRecompensa(
    val id: String,
    val actual: Int,
    val meta: Int,
    val desbloqueada: Boolean
) {
    val progreso: Float
        get() = if (meta <= 0) 1f else (actual.toFloat() / meta).coerceIn(0f, 1f)
}

/**
 * Decide qué insignias y coleccionables están desbloqueados.
 * Todo se deriva del [EstadoJuego], nunca de valores escritos a mano.
 */
object EvaluadorRecompensas {

    fun evaluarInsignias(
        estado: EstadoJuego,
        insignias: List<Insignia>
    ): List<ProgresoRecompensa> = insignias.map {
        ProgresoRecompensa(
            id = it.id,
            actual = Medidor.valor(estado, it.requisito),
            meta = it.requisito.meta,
            desbloqueada = Medidor.cumplido(estado, it.requisito)
        )
    }

    /**
     * Los coleccionables se evalúan en dos pasadas: primero los que dependen
     * del juego y después los que dependen del tamaño de la propia colección,
     * usando el recuento ya obtenido en la primera pasada.
     */
    fun evaluarColeccionables(
        estado: EstadoJuego,
        coleccionables: List<Coleccionable>
    ): List<ProgresoRecompensa> {
        val directos = coleccionables.filter { it.requisito.medida != Medida.COLECCIONABLES }
        val dependientes = coleccionables.filter { it.requisito.medida == Medida.COLECCIONABLES }

        val primeraPasada = directos.map {
            ProgresoRecompensa(
                id = it.id,
                actual = Medidor.valor(estado, it.requisito),
                meta = it.requisito.meta,
                desbloqueada = Medidor.cumplido(estado, it.requisito)
            )
        }
        val conseguidos = primeraPasada.count { it.desbloqueada }
        val estadoConRecuento = estado.copy(coleccionablesDesbloqueados = conseguidos)

        val segundaPasada = dependientes.map {
            ProgresoRecompensa(
                id = it.id,
                actual = Medidor.valor(estadoConRecuento, it.requisito),
                meta = it.requisito.meta,
                desbloqueada = Medidor.cumplido(estadoConRecuento, it.requisito)
            )
        }
        val porId = (primeraPasada + segundaPasada).associateBy { it.id }
        return coleccionables.mapNotNull { porId[it.id] }
    }

    /** Ids recién desbloqueados respecto a lo que ya estaba guardado. */
    fun nuevosDesbloqueos(
        progresos: List<ProgresoRecompensa>,
        yaDesbloqueados: Set<String>
    ): List<String> =
        progresos.filter { it.desbloqueada && it.id !in yaDesbloqueados }.map { it.id }
}

/**
 * Reglas de apertura del mapa.
 *
 * Una zona se abre cuando el jugador alcanza la experiencia mínima y ha
 * completado con al menos una estrella la zona anterior. La primera zona
 * (Casa) siempre está disponible.
 */
object DesbloqueoZonas {

    fun estado(zona: ZonaId, juego: EstadoJuego): EstadoZona {
        val resumen = juego.zonas[zona]
        val estrellas = resumen?.estrellas ?: 0

        if (estrellas >= 3) return EstadoZona.DOMINADA
        if (estrellas >= 1) return EstadoZona.COMPLETADA

        if (!estaAbierta(zona, juego)) return EstadoZona.BLOQUEADA
        if (resumen?.iniciada == true) return EstadoZona.EN_PROGRESO
        return EstadoZona.DISPONIBLE
    }

    fun estaAbierta(zona: ZonaId, juego: EstadoJuego): Boolean {
        if (zona.orden == 1) return true
        if (juego.xpTotal < zona.xpRequerido) return false
        val anterior = ZonaId.enOrden.firstOrNull { it.orden == zona.orden - 1 } ?: return true
        return juego.estrellasEn(anterior) >= 1
    }

    /** Texto que explica al niño qué le falta para abrir la zona. */
    fun requisitoPendiente(zona: ZonaId, juego: EstadoJuego): String? {
        if (estaAbierta(zona, juego)) return null
        val anterior = ZonaId.enOrden.firstOrNull { it.orden == zona.orden - 1 }
        if (anterior != null && juego.estrellasEn(anterior) < 1) {
            return "Completa la misión de " + anterior.titulo + " para abrir esta zona."
        }
        val faltan = zona.xpRequerido - juego.xpTotal
        return "Te faltan " + faltan + " XP para abrir esta zona."
    }

    fun estadoDeTodas(juego: EstadoJuego): Map<ZonaId, EstadoZona> =
        ZonaId.enOrden.associateWith { estado(it, juego) }
}
