package pe.ecoguardianes.data.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Perfil local del jugador. Fila única (id = 1).
 * Solo guarda un alias inventado y preferencias: ningún dato personal.
 */
@Entity(tableName = "perfil")
data class PerfilEntity(
    @PrimaryKey val id: Int = ID_UNICO,
    val alias: String = "",
    val avatarId: String = "AV_HOJA",
    val xp: Int = 0,
    val onboardingCompletado: Boolean = false,
    val sonidoActivado: Boolean = true,
    val hapticaActivada: Boolean = true,
    val textoGrande: Boolean = false,
    val pistasAutomaticas: Boolean = true,
    val creadoEn: Long = 0L
) {
    companion object {
        const val ID_UNICO = 1
    }
}

/** Progreso acumulado del jugador en una zona del mapa. */
@Entity(tableName = "progreso_zona")
data class ProgresoZonaEntity(
    @PrimaryKey val zonaId: String,
    val estrellas: Int = 0,
    val mejorPuntaje: Int = 0,
    val vecesCompletada: Int = 0,
    val iniciada: Boolean = false,
    val actualizadoEn: Long = 0L
)

/** Ficha de auditoría guardada al terminar una misión. */
@Entity(
    tableName = "auditoria",
    indices = [Index("zonaId"), Index("misionId")]
)
data class AuditoriaEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val misionId: String,
    val zonaId: String,
    val iniciadaEn: Long,
    val finalizadaEn: Long,
    val totalSituaciones: Int,
    val problemasTotales: Int,
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
    val completada: Boolean = true
)

/**
 * Hallazgo individual registrado dentro de una auditoría.
 * Se guardan también los falsos positivos: forman parte de la evidencia.
 */
@Entity(
    tableName = "hallazgo",
    foreignKeys = [
        ForeignKey(
            entity = AuditoriaEntity::class,
            parentColumns = ["id"],
            childColumns = ["auditoriaId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("auditoriaId"), Index("categoria"), Index("situacionId")]
)
data class HallazgoEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val auditoriaId: Long,
    val situacionId: String,
    val zonaId: String,
    val nombre: String,
    val categoria: String,
    val gravedad: String,
    val categoriaElegida: String?,
    val gravedadElegida: String?,
    val descripcion: String,
    val reglaId: String,
    val accionId: String?,
    val accionCorrecta: Boolean,
    val retoSuperado: Boolean,
    val intentos: Int,
    @ColumnInfo(name = "valido") val valido: Boolean,
    val estado: String,
    val registradoEn: Long
)

/** Estado de desbloqueo de una insignia. */
@Entity(tableName = "insignia")
data class InsigniaEntity(
    @PrimaryKey val id: String,
    val desbloqueada: Boolean = false,
    val desbloqueadaEn: Long? = null,
    val progresoActual: Int = 0,
    val meta: Int = 0
)

/** Estado de desbloqueo de un elemento de la colección. */
@Entity(tableName = "coleccionable")
data class ColeccionableEntity(
    @PrimaryKey val id: String,
    val desbloqueado: Boolean = false,
    val desbloqueadoEn: Long? = null,
    val progresoActual: Int = 0,
    val meta: Int = 0
)

/** Resultado de una consulta agregada de hallazgos por categoría. */
data class ConteoCategoria(
    val categoria: String,
    val total: Int
)
