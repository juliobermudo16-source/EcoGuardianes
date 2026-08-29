package pe.ecoguardianes.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Base de datos local de EcoGuardianes.
 *
 * Todo el progreso vive aquí: no hay listas en memoria haciendo de sustituto.
 * La aplicación funciona sin conexión y no envía nada fuera del dispositivo.
 */
@Database(
    entities = [
        PerfilEntity::class,
        ProgresoZonaEntity::class,
        AuditoriaEntity::class,
        HallazgoEntity::class,
        InsigniaEntity::class,
        ColeccionableEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class EcoDatabase : RoomDatabase() {

    abstract fun perfilDao(): PerfilDao
    abstract fun progresoZonaDao(): ProgresoZonaDao
    abstract fun auditoriaDao(): AuditoriaDao
    abstract fun hallazgoDao(): HallazgoDao
    abstract fun insigniaDao(): InsigniaDao
    abstract fun coleccionableDao(): ColeccionableDao

    companion object {
        const val NOMBRE = "ecoguardianes.db"

        @Volatile
        private var instancia: EcoDatabase? = null

        fun obtener(context: Context): EcoDatabase =
            instancia ?: synchronized(this) {
                instancia ?: construir(context.applicationContext).also { instancia = it }
            }

        private fun construir(context: Context): EcoDatabase =
            Room.databaseBuilder(context, EcoDatabase::class.java, NOMBRE)
                .fallbackToDestructiveMigration()
                .build()
    }
}
