package pl.technoviking.data.model

import pl.technoviking.local.neo.NeoEntity

data class NeoSimple(
    val id: String,
    val name: String,
    val diameterMinKm: String,
    val diameterMaxKm: String,
    val isDangerous: Boolean,
)