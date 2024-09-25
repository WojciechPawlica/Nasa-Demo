package pl.technoviking.remote.api.model.neo


import com.google.gson.annotations.SerializedName

data class MissDistance(
    @SerializedName("astronomical")
    val astronomical: String,
    @SerializedName("lunar")
    val lunar: String,
    @SerializedName("kilometers")
    val kilometers: String,
    @SerializedName("miles")
    val miles: String
)