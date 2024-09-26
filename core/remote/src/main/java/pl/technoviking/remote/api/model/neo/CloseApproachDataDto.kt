package pl.technoviking.remote.api.model.neo


import com.google.gson.annotations.SerializedName

data class CloseApproachDataDto(
    @SerializedName("close_approach_date")
    val closeApproachDate: String,
    @SerializedName("close_approach_date_full")
    val closeApproachDateFull: String,
    @SerializedName("epoch_date_close_approach")
    val epochDateCloseApproach: Long,
    @SerializedName("relative_velocity")
    val relativeVelocity: RelativeVelocityDto,
    @SerializedName("miss_distance")
    val missDistance: MissDistanceDto,
    @SerializedName("orbiting_body")
    val orbitingBody: String
)

data class RelativeVelocityDto(
    @SerializedName("kilometers_per_second")
    val kilometersPerSecond: String,
    @SerializedName("kilometers_per_hour")
    val kilometersPerHour: String,
    @SerializedName("miles_per_hour")
    val milesPerHour: String
)

data class MissDistanceDto(
    @SerializedName("astronomical")
    val astronomical: String,
    @SerializedName("lunar")
    val lunar: String,
    @SerializedName("kilometers")
    val kilometers: String,
    @SerializedName("miles")
    val miles: String
)