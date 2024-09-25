package pl.technoviking.data.model

data class NeoExtended(
    val id: String,
    val name: String,
    val absoluteMagnitudeH: Double,
    val estimatedDiameter: EstimatedDiameter,
    val isPotentiallyHazardousAsteroid: Boolean,
    val closeApproachData: List<CloseApproachData>,
    val isSentryObject: Boolean
)

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

data class CloseApproachData(
    val closeApproachDate: String,
    val closeApproachDateFull: String,
    val epochDateCloseApproach: Long,
    val relativeVelocity: RelativeVelocity,
    val missDistance: MissDistance,
    val orbitingBody: String
)

data class RelativeVelocity(
    val kilometersPerSecond: String,
    val kilometersPerHour: String,
    val milesPerHour: String
)

data class MissDistance(
    val astronomical: String,
    val lunar: String,
    val kilometers: String,
    val miles: String
)