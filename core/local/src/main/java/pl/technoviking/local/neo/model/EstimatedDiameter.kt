package pl.technoviking.local.neo.model

import androidx.room.TypeConverter

data class EstimatedDiameter(
    val kilometers: EstimatedDiameterValues,
    val meters: EstimatedDiameterValues,
    val miles: EstimatedDiameterValues,
    val feet: EstimatedDiameterValues
)

data class EstimatedDiameterValues(
    val estimatedDiameterMin: Double,
    val estimatedDiameterMax: Double
)

class EstimatedDiameterValuesConverter {

    @TypeConverter
    fun fromEstimatedDiameterValues(value: EstimatedDiameterValues): String {
        return with(value) { "$estimatedDiameterMin$SEPARATOR$estimatedDiameterMax" }
    }

    @TypeConverter
    fun toEstimatedDiameterValues(value: String): EstimatedDiameterValues {
        return with(value.split(SEPARATOR)) {
            EstimatedDiameterValues(get(0).toDouble(), get(1).toDouble())
        }
    }

    companion object {
        private const val SEPARATOR = "|"
    }
}
