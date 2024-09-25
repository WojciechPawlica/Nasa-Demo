package pl.technoviking.remote.api.model.neo


import com.google.gson.annotations.SerializedName
import pl.technoviking.remote.api.model.Links

data class NeoDto(
    @SerializedName("links")
    val links: Links,
    @SerializedName("id")
    val id: String,
    @SerializedName("neo_reference_id")
    val neoReferenceId: String,
    @SerializedName("name")
    val name: String,
    @SerializedName("nasa_jpl_url")
    val nasaJplUrl: String,
    @SerializedName("absolute_magnitude_h")
    val absoluteMagnitudeH: Double,
    @SerializedName("estimated_diameter")
    val estimatedDiameter: EstimatedDiameterDto,
    @SerializedName("is_potentially_hazardous_asteroid")
    val isPotentiallyHazardousAsteroid: Boolean,
    @SerializedName("close_approach_data")
    val closeApproachData: List<CloseApproachDataDto>,
    @SerializedName("is_sentry_object")
    val isSentryObject: Boolean
)