package pl.technoviking.remote.api.model.neo


import com.google.gson.annotations.SerializedName

data class EstimatedDiameterDto(
    @SerializedName("kilometers")
    val kilometers: EstimatedDiameterValuesDto,
    @SerializedName("meters")
    val meters: EstimatedDiameterValuesDto,
    @SerializedName("miles")
    val miles: EstimatedDiameterValuesDto,
    @SerializedName("feet")
    val feet: EstimatedDiameterValuesDto
)

data class EstimatedDiameterValuesDto(
    @SerializedName("estimated_diameter_min")
    val estimatedDiameterMin: Double,
    @SerializedName("estimated_diameter_max")
    val estimatedDiameterMax: Double
)