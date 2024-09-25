package pl.technoviking.remote.api.model.neo


import com.google.gson.annotations.SerializedName

data class EstimatedDiameter(
    @SerializedName("kilometers")
    val kilometers: Kilometers,
    @SerializedName("meters")
    val meters: Meters,
    @SerializedName("miles")
    val miles: Miles,
    @SerializedName("feet")
    val feet: Feet
)