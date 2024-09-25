package pl.technoviking.data.model.mapper

import pl.technoviking.data.model.CloseApproachData
import pl.technoviking.data.model.EstimatedDiameter
import pl.technoviking.data.model.EstimatedDiameterValues
import pl.technoviking.data.model.MissDistance
import pl.technoviking.data.model.NeoExtended
import pl.technoviking.data.model.NeoSimple
import pl.technoviking.data.model.RelativeVelocity
import pl.technoviking.local.neo.NeoEntity
import pl.technoviking.local.neo.model.CloseApproachDataLocal
import pl.technoviking.local.neo.model.EstimatedDiameterLocal
import pl.technoviking.local.neo.model.EstimatedDiameterValuesLocal
import pl.technoviking.local.neo.model.MissDistanceLocal
import pl.technoviking.local.neo.model.RelativeVelocityLocal

internal fun NeoEntity.toNeoSimple() = NeoSimple(
    id = id,
    name = name,
    diameterMinKm = estimatedDiameter.kilometers.estimatedDiameterMin.toString(),
    diameterMaxKm = estimatedDiameter.kilometers.estimatedDiameterMax.toString(),
    isDangerous = isPotentiallyHazardousAsteroid
)

internal fun NeoEntity.toNeoExtended() = NeoExtended(
    id = id,
    name = name,
    absoluteMagnitudeH = absoluteMagnitudeH,
    estimatedDiameter = estimatedDiameter.toDomain(),
    isPotentiallyHazardousAsteroid = isPotentiallyHazardousAsteroid,
    closeApproachData = closeApproachData.toDomain(),
    isSentryObject = isSentryObject
)

internal fun EstimatedDiameterLocal.toDomain() = EstimatedDiameter(
    kilometers = kilometers.toDomain(),
    meters = meters.toDomain(),
    miles = miles.toDomain(),
    feet = feet.toDomain()
)

internal fun EstimatedDiameterValuesLocal.toDomain() = EstimatedDiameterValues(
    estimatedDiameterMin = estimatedDiameterMin, estimatedDiameterMax = estimatedDiameterMax
)

internal fun CloseApproachDataLocal.toDomain() = CloseApproachData(
    closeApproachDate = closeApproachDate,
    closeApproachDateFull = closeApproachDateFull,
    epochDateCloseApproach = epochDateCloseApproach,
    relativeVelocity = relativeVelocity.toDomain(),
    missDistance = missDistance.toDomain(),
    orbitingBody = orbitingBody
)

internal fun List<CloseApproachDataLocal>.toDomain() = map(CloseApproachDataLocal::toDomain)

internal fun RelativeVelocityLocal.toDomain() = RelativeVelocity(
    kilometersPerSecond = kilometersPerSecond,
    kilometersPerHour = kilometersPerHour,
    milesPerHour = milesPerHour
)

internal fun MissDistanceLocal.toDomain() = MissDistance(
    astronomical = astronomical, lunar = lunar, kilometers = kilometers, miles = miles
)