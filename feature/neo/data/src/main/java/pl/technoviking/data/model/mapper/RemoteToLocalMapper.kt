package pl.technoviking.data.model.mapper

import pl.technoviking.local.neo.NeoEntity
import pl.technoviking.local.neo.model.CloseApproachDataLocal
import pl.technoviking.local.neo.model.EstimatedDiameterLocal
import pl.technoviking.local.neo.model.EstimatedDiameterValuesLocal
import pl.technoviking.local.neo.model.MissDistanceLocal
import pl.technoviking.local.neo.model.RelativeVelocityLocal
import pl.technoviking.remote.api.model.NearEarthObjectsResponse
import pl.technoviking.remote.api.model.neo.CloseApproachDataDto
import pl.technoviking.remote.api.model.neo.EstimatedDiameterDto
import pl.technoviking.remote.api.model.neo.EstimatedDiameterValuesDto
import pl.technoviking.remote.api.model.neo.MissDistanceDto
import pl.technoviking.remote.api.model.neo.NeoDto
import pl.technoviking.remote.api.model.neo.RelativeVelocityDto

internal fun NearEarthObjectsResponse.toLocal(): List<NeoEntity> =
    nearEarthObjects.map { it.value.toLocal(it.key) }.flatten()

internal fun NeoDto.toLocal(date: String) = NeoEntity(
    date = date,
    id = id,
    neoReferenceId = neoReferenceId,
    name = name,
    nasaJplUrl = nasaJplUrl,
    absoluteMagnitudeH = absoluteMagnitudeH,
    estimatedDiameter = estimatedDiameter.toLocal(),
    isPotentiallyHazardousAsteroid = isPotentiallyHazardousAsteroid,
    closeApproachData = closeApproachData.toLocal(),
    isSentryObject = isSentryObject
)

internal fun List<NeoDto>.toLocal(date: String) = map { it.toLocal(date) }

internal fun EstimatedDiameterDto.toLocal() = EstimatedDiameterLocal(
    kilometers = kilometers.toLocal(),
    meters = meters.toLocal(),
    miles = miles.toLocal(),
    feet = feet.toLocal()
)

internal fun EstimatedDiameterValuesDto.toLocal() = EstimatedDiameterValuesLocal(
    estimatedDiameterMin = estimatedDiameterMin, estimatedDiameterMax = estimatedDiameterMax
)

internal fun CloseApproachDataDto.toLocal() = CloseApproachDataLocal(
    closeApproachDate = closeApproachDate,
    closeApproachDateFull = closeApproachDateFull,
    epochDateCloseApproach = epochDateCloseApproach,
    relativeVelocity = relativeVelocity.toLocal(),
    missDistance = missDistance.toLocal(),
    orbitingBody = orbitingBody
)

internal fun List<CloseApproachDataDto>.toLocal() = map(CloseApproachDataDto::toLocal)

internal fun RelativeVelocityDto.toLocal() = RelativeVelocityLocal(
    kilometersPerSecond = kilometersPerSecond,
    kilometersPerHour = kilometersPerHour,
    milesPerHour = milesPerHour
)

internal fun MissDistanceDto.toLocal() = MissDistanceLocal(
    astronomical = astronomical, lunar = lunar, kilometers = kilometers, miles = miles
)