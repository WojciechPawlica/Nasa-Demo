package pl.technoviking.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import pl.technoviking.local.neo.NeoDao
import pl.technoviking.local.neo.NeoEntity
import pl.technoviking.local.neo.model.CloseApproachDataConverter
import pl.technoviking.local.neo.model.EstimatedDiameterValuesConverter

@Database(entities = [NeoEntity::class], version = 1, exportSchema = false)
@TypeConverters(value = [EstimatedDiameterValuesConverter::class, CloseApproachDataConverter::class])
abstract class LocalDatabase : RoomDatabase() {

    abstract fun neoDao(): NeoDao
}