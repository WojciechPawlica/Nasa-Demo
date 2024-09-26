package pl.technoviking.local.neo

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NeoDao {

    @Query("SELECT * FROM neo")
    fun observe() : Flow<List<NeoEntity>>

    @Query("SELECT * FROM neo WHERE id = :id")
    fun getById(id: String): NeoEntity

    @Upsert
    suspend fun upsert(characters : List<NeoEntity>)

    @Query("DELETE FROM neo")
    suspend fun clear()
}