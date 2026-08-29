package pe.ecoguardianes.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PerfilDao {

    @Query("SELECT * FROM perfil WHERE id = :id LIMIT 1")
    fun observar(id: Int = PerfilEntity.ID_UNICO): Flow<PerfilEntity?>

    @Query("SELECT * FROM perfil WHERE id = :id LIMIT 1")
    suspend fun obtener(id: Int = PerfilEntity.ID_UNICO): PerfilEntity?

    @Upsert
    suspend fun guardar(perfil: PerfilEntity)

    @Query("UPDATE perfil SET xp = xp + :delta WHERE id = :id")
    suspend fun sumarXp(delta: Int, id: Int = PerfilEntity.ID_UNICO)

    @Query("UPDATE perfil SET onboardingCompletado = 1 WHERE id = :id")
    suspend fun marcarOnboarding(id: Int = PerfilEntity.ID_UNICO)

    @Query("DELETE FROM perfil")
    suspend fun borrarTodo()
}

@Dao
interface ProgresoZonaDao {

    @Query("SELECT * FROM progreso_zona")
    fun observarTodas(): Flow<List<ProgresoZonaEntity>>

    @Query("SELECT * FROM progreso_zona WHERE zonaId = :zonaId LIMIT 1")
    suspend fun obtener(zonaId: String): ProgresoZonaEntity?

    @Upsert
    suspend fun guardar(progreso: ProgresoZonaEntity)

    @Query("DELETE FROM progreso_zona")
    suspend fun borrarTodo()
}

@Dao
interface AuditoriaDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertar(auditoria: AuditoriaEntity): Long

    @Query("SELECT * FROM auditoria WHERE id = :id LIMIT 1")
    suspend fun obtener(id: Long): AuditoriaEntity?

    @Query("SELECT * FROM auditoria ORDER BY finalizadaEn DESC")
    fun observarTodas(): Flow<List<AuditoriaEntity>>

    @Query("SELECT * FROM auditoria WHERE zonaId = :zonaId ORDER BY finalizadaEn DESC")
    fun observarDeZona(zonaId: String): Flow<List<AuditoriaEntity>>

    @Query("SELECT COUNT(*) FROM auditoria WHERE completada = 1")
    fun contarCompletadas(): Flow<Int>

    @Query("SELECT COUNT(*) FROM auditoria WHERE completada = 1 AND deteccionPerfecta = 1")
    fun contarDeteccionPerfecta(): Flow<Int>

    @Query("SELECT COALESCE(MAX(puntaje), 0) FROM auditoria WHERE completada = 1")
    fun puntajeMaximo(): Flow<Int>

    @Query("SELECT COALESCE(SUM(xpGanado), 0) FROM auditoria WHERE completada = 1")
    suspend fun xpAcumulado(): Int

    @Query("DELETE FROM auditoria")
    suspend fun borrarTodo()
}

@Dao
interface HallazgoDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertarTodos(hallazgos: List<HallazgoEntity>): List<Long>

    @Query("SELECT * FROM hallazgo WHERE auditoriaId = :auditoriaId ORDER BY id ASC")
    suspend fun deAuditoria(auditoriaId: Long): List<HallazgoEntity>

    @Query("SELECT * FROM hallazgo WHERE auditoriaId = :auditoriaId ORDER BY id ASC")
    fun observarDeAuditoria(auditoriaId: Long): Flow<List<HallazgoEntity>>

    @Query(
        "SELECT categoria AS categoria, COUNT(*) AS total FROM hallazgo " +
            "WHERE valido = 1 GROUP BY categoria"
    )
    fun conteoPorCategoria(): Flow<List<ConteoCategoria>>

    @Query("SELECT COUNT(*) FROM hallazgo WHERE valido = 1 AND accionCorrecta = 1")
    fun contarAccionesCorrectas(): Flow<Int>

    @Query("SELECT COUNT(*) FROM hallazgo WHERE valido = 1")
    fun contarValidos(): Flow<Int>

    @Query("DELETE FROM hallazgo")
    suspend fun borrarTodo()
}

@Dao
interface InsigniaDao {

    @Query("SELECT * FROM insignia")
    fun observarTodas(): Flow<List<InsigniaEntity>>

    @Query("SELECT * FROM insignia WHERE desbloqueada = 1")
    suspend fun desbloqueadas(): List<InsigniaEntity>

    @Upsert
    suspend fun guardarTodas(insignias: List<InsigniaEntity>)

    @Query("DELETE FROM insignia")
    suspend fun borrarTodo()
}

@Dao
interface ColeccionableDao {

    @Query("SELECT * FROM coleccionable")
    fun observarTodos(): Flow<List<ColeccionableEntity>>

    @Query("SELECT COUNT(*) FROM coleccionable WHERE desbloqueado = 1")
    fun contarDesbloqueados(): Flow<Int>

    @Upsert
    suspend fun guardarTodos(coleccionables: List<ColeccionableEntity>)

    @Query("DELETE FROM coleccionable")
    suspend fun borrarTodo()
}
