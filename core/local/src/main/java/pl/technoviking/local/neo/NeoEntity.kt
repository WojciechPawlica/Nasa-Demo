package pl.technoviking.local.neo

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import pl.technoviking.local.neo.model.CloseApproachData
import pl.technoviking.local.neo.model.EstimatedDiameter

@Entity(tableName = "neo")
data class NeoEntity(
    val date: String,
    @PrimaryKey val id: String,
    val neoReferenceId: String,
    val name: String,
    val nasaJplUrl: String,
    val absoluteMagnitudeH: Double,
    @Embedded val estimatedDiameter: EstimatedDiameter,
    val isPotentiallyHazardousAsteroid: Boolean,
    val closeApproachData: List<CloseApproachData>,
    val isSentryObject: Boolean
)