package pl.technoviking.presentation.di

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import pl.technoviking.data.NeoRepository
import pl.technoviking.data.di.RepositoryModule
import pl.technoviking.data.model.EstimatedDiameter
import pl.technoviking.data.model.EstimatedDiameterValues
import pl.technoviking.data.model.NeoExtended
import pl.technoviking.data.model.NeoSimple
import java.time.LocalDate

@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)

@Module
object FakeRepositoryModule {

    @Provides
    fun providePinRepository() = object : NeoRepository {

        private val flow = MutableStateFlow(emptyMap<String, List<NeoSimple>>())

        override suspend fun fetchData(startDate: LocalDate, endDate: LocalDate) {
            delay(1000L)
            flow.value += mapOf(
                "1970-01-01" to listOf(
                    NeoSimple(
                        id = "1",
                        name = "Asteroid",
                        diameterMinKm = 176.177,
                        diameterMaxKm = 178.179,
                        isDangerous = false
                    ),
                    NeoSimple(
                        id = "2",
                        name = "Comet",
                        diameterMinKm = 176.177,
                        diameterMaxKm = 178.179,
                        isDangerous = false
                    ),
                    NeoSimple(
                        id = "3",
                        name = "Moon",
                        diameterMinKm = 176.177,
                        diameterMaxKm = 178.179,
                        isDangerous = true
                    ),
                    NeoSimple(
                        id = "4",
                        name = "Asteroid",
                        diameterMinKm = 176.177,
                        diameterMaxKm = 178.179,
                        isDangerous = false
                    ),
                    NeoSimple(
                        id = "5",
                        name = "Comet",
                        diameterMinKm = 176.177,
                        diameterMaxKm = 178.179,
                        isDangerous = false
                    ),
                )
            )
        }

        override fun observeNeoSimple(): Flow<Map<String, List<NeoSimple>>> = flow.asStateFlow()

        override suspend fun getNeoExtended(id: String) = NeoExtended(
            id = "1",
            name = "Asteroid",
            absoluteMagnitudeH = 198.199,
            estimatedDiameter = EstimatedDiameter(
                kilometers = EstimatedDiameterValues(
                    estimatedDiameterMin = 200.201,
                    estimatedDiameterMax = 202.203
                ), meters = EstimatedDiameterValues(
                    estimatedDiameterMin = 204.205,
                    estimatedDiameterMax = 206.207
                ), miles = EstimatedDiameterValues(
                    estimatedDiameterMin = 208.209,
                    estimatedDiameterMax = 210.211
                ), feet = EstimatedDiameterValues(
                    estimatedDiameterMin = 212.213,
                    estimatedDiameterMax = 214.215
                )
            ),
            isPotentiallyHazardousAsteroid = false,
            closeApproachData = listOf(),
            isSentryObject = false
        )
    }
}