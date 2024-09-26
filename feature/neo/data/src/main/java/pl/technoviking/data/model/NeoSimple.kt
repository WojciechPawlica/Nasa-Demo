package pl.technoviking.data.model

data class NeoSimple(
    val id: String,
    val name: String,
    val diameterMinKm: Double,
    val diameterMaxKm: Double,
    val isDangerous: Boolean,
)