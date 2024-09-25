package pl.technoviking.local.neo.model

import androidx.room.TypeConverter

data class EstimatedDiameterLocal(
    val kilometers: EstimatedDiameterValuesLocal,
    val meters: EstimatedDiameterValuesLocal,
    val miles: EstimatedDiameterValuesLocal,
    val feet: EstimatedDiameterValuesLocal
)

data class EstimatedDiameterValuesLocal(
    val estimatedDiameterMin: Double,
    val estimatedDiameterMax: Double
)

internal class EstimatedDiameterValuesConverter {

    @TypeConverter
    fun fromEstimatedDiameterValues(value: EstimatedDiameterValuesLocal): String {
        return with(value) { "$estimatedDiameterMin$SEPARATOR$estimatedDiameterMax" }
    }

    @TypeConverter
    fun toEstimatedDiameterValues(value: String): EstimatedDiameterValuesLocal {
        return with(value.split(SEPARATOR)) {
            EstimatedDiameterValuesLocal(get(0).toDouble(), get(1).toDouble())
        }
    }

    companion object {
        private const val SEPARATOR = "|"
    }
}
