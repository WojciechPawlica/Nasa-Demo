package pl.technoviking.local.neo.model


import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

data class CloseApproachData(
    val closeApproachDate: String,
    val closeApproachDateFull: String,
    val epochDateCloseApproach: Long,
    val relativeVelocity: RelativeVelocity,
    val missDistance: MissDistance,
    val orbitingBody: String
)

data class RelativeVelocity(
    val kilometersPerSecond: String,
    val kilometersPerHour: String,
    val milesPerHour: String
)

data class MissDistance(
    val astronomical: String,
    val lunar: String,
    val kilometers: String,
    val miles: String
)

class CloseApproachDataConverter {

    private val gson = Gson()
    private val typeToken = object : TypeToken<List<CloseApproachData>>() {}.type

    @TypeConverter
    fun fromCloseApproachDataList(value: List<CloseApproachData>): String {
        return gson.toJson(value, typeToken)
    }

    @TypeConverter
    fun toCloseApproachDataList(value: String): List<CloseApproachData> {
        return gson.fromJson(value, typeToken)
    }
}