package pe.ecoguardianes.domain

import pe.ecoguardianes.domain.model.Categoria
import pe.ecoguardianes.domain.model.Gravedad
import pe.ecoguardianes.domain.model.IconoAmb
import pe.ecoguardianes.domain.model.Mision
import pe.ecoguardianes.domain.model.Situacion
import pe.ecoguardianes.domain.model.ZonaId

/** Datos de prueba reutilizables por los tests del motor de auditoría. */
object Fixtures {

    fun situacion(
        id: String,
        gravedad: Gravedad = Gravedad.NO_CONFORMIDAD,
        categoria: Categoria = Categoria.RESIDUOS,
        xp: Int = 20
    ) = Situacion(
        id = id,
        zona = ZonaId.CASA,
        nombre = "Situación " + id,
        icono = IconoAmb.BOLSA_BASURA,
        x = 0.5f,
        y = 0.5f,
        categoria = categoria,
        gravedad = gravedad,
        observacion = "Observación de prueba",
        explicacion = "Explicación de prueba",
        pista = "Pista de prueba",
        reglaId = "R_RES_01",
        accionesIds = listOf(id + "_A1", id + "_A2", id + "_A3"),
        retoId = null,
        xp = xp
    )

    fun mision(
        situaciones: List<Situacion>,
        minimo: Int = 1,
        id: String = "M_TEST"
    ) = Mision(
        id = id,
        zona = ZonaId.CASA,
        titulo = "Misión de prueba",
        briefingEco = "Briefing",
        objetivo = "Objetivo",
        situacionesIds = situaciones.map { it.id },
        minimoHallazgos = minimo,
        nivel = 1
    )
}
