package pl.technoviking.remote.api.model


import com.google.gson.annotations.SerializedName
import pl.technoviking.remote.api.model.neo.NearEarthObject

data class NearEarthObjectsResponse(
    @SerializedName("links")
    val links: Links,
    @SerializedName("element_count")
    val elementCount: Int,
    @SerializedName("near_earth_objects")
    val nearEarthObjects: Map<String, List<NearEarthObject>>
)