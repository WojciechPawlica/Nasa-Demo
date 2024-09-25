package pl.technoviking.remote.api.model.neo


import com.google.gson.annotations.SerializedName

data class EstimatedDiameter(
    @SerializedName("kilometers")
    val kilometers: EstimatedDiameterValues,
    @SerializedName("meters")
    val meters: EstimatedDiameterValues,
    @SerializedName("miles")
    val miles: EstimatedDiameterValues,
    @SerializedName("feet")
    val feet: EstimatedDiameterValues
)

data class EstimatedDiameterValues(
    @SerializedName("estimated_diameter_min")
    val estimatedDiameterMin: Double,
    @SerializedName("estimated_diameter_max")
    val estimatedDiameterMax: Double
)