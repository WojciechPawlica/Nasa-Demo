@file:OptIn(ExperimentalCoroutinesApi::class)

package pl.technoviking.presentation.details

import androidx.lifecycle.SavedStateHandle
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import pl.technoviking.data.NeoRepository
import pl.technoviking.data.model.EstimatedDiameter
import pl.technoviking.data.model.EstimatedDiameterValues
import pl.technoviking.data.model.NeoExtended

class NeoDetailsViewModelTest {

    private val repository = mockk<NeoRepository>()
    private val savedStateHandle = mockk<SavedStateHandle>()

    private lateinit var viewModel: NeoDetailsViewModel

    @Before
    fun setup() {
        every { savedStateHandle.get<String>("id") } returns ID
        coEvery { repository.getNeoExtended(any()) } returns DUMMY_NEO_EXTENDED
        viewModel = NeoDetailsViewModel(repository, UnconfinedTestDispatcher(), savedStateHandle)
    }

    @Test
    fun `Test calling getNeoExtended at initialization`() {
        coVerify(exactly = 1) { repository.getNeoExtended(ID) }
    }

    companion object {
        const val ID = "ID"

        val DUMMY_NEO_EXTENDED = NeoExtended(
            id = "discere",
            name = "Sophie Guy",
            absoluteMagnitudeH = 150.151,
            estimatedDiameter = EstimatedDiameter(
                kilometers = EstimatedDiameterValues(
                    estimatedDiameterMin = 152.153,
                    estimatedDiameterMax = 154.155
                ), meters = EstimatedDiameterValues(
                    estimatedDiameterMin = 156.157,
                    estimatedDiameterMax = 158.159
                ), miles = EstimatedDiameterValues(
                    estimatedDiameterMin = 160.161,
                    estimatedDiameterMax = 162.163
                ), feet = EstimatedDiameterValues(
                    estimatedDiameterMin = 164.165,
                    estimatedDiameterMax = 166.167
                )
            ),
            isPotentiallyHazardousAsteroid = false,
            closeApproachData = listOf(),
            isSentryObject = false
        )
    }
}