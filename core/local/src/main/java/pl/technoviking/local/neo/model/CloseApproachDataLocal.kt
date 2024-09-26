package pl.technoviking.local.neo.model


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class CloseApproachDataLocal(
    val closeApproachDate: String,
    val closeApproachDateFull: String,
    val epochDateCloseApproach: Long,
    val relativeVelocity: RelativeVelocityLocal,
    val missDistance: MissDistanceLocal,
    val orbitingBody: String
)

data class RelativeVelocityLocal(
    val kilometersPerSecond: String,
    val kilometersPerHour: String,
    val milesPerHour: String
)

data class MissDistanceLocal(
    val astronomical: String,
    val lunar: String,
    val kilometers: String,
    val miles: String
)

internal class CloseApproachDataConverter {

    private val gson = Gson()
    private val typeToken = object : TypeToken<List<CloseApproachDataLocal>>() {}.type

    @TypeConverter
    fun fromCloseApproachDataList(value: List<CloseApproachDataLocal>): String {
        return gson.toJson(value, typeToken)
    }

    @TypeConverter
    fun toCloseApproachDataList(value: String): List<CloseApproachDataLocal> {
        return gson.fromJson(value, typeToken)
    }
}