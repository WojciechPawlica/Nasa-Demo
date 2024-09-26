package pl.technoviking.data

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import org.junit.Before
import org.junit.Test
import pl.technoviking.data.model.mapper.toNeoSimple
import pl.technoviking.local.neo.NeoDao
import pl.technoviking.local.neo.NeoEntity
import pl.technoviking.local.neo.model.EstimatedDiameterLocal
import pl.technoviking.local.neo.model.EstimatedDiameterValuesLocal
import pl.technoviking.remote.api.NeoApi
import pl.technoviking.remote.api.model.Links
import pl.technoviking.remote.api.model.NearEarthObjectsResponse
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalCoroutinesApi::class)
class NeoRepositoryImplTest {

    private val neoDao = mockk<NeoDao>()
    private val neoApi = mockk<NeoApi>()

    private lateinit var repository: NeoRepository

    @Before
    fun setUp() {
        coEvery { neoDao.upsert(any()) } returns Unit
        coEvery { neoDao.clear() } returns Unit
        coEvery { neoDao.getById(any()) } returns DUMMY_NEO_ENTITY
        coEvery { neoApi.getNearEarthObjectsResponse(any(), any()) } returns DUMMY_RESPONSE
        repository = NeoRepositoryImpl(
            neoDao,
            neoApi,
            DateTimeFormatter.ISO_DATE,
            UnconfinedTestDispatcher()
        )
    }

    @Test
    fun `test fetchData`() {
        runBlocking {
            repository.fetchData(LocalDate.now(), LocalDate.now())
            coVerify(exactly = 1) { neoApi.getNearEarthObjectsResponse(any(), any()) }
            coVerify(exactly = 1) { neoDao.upsert(any()) }
            coVerify(exactly = 1) { neoDao.clear() }
        }
    }

    @Test
    fun `test getNeoExtended`() {
        runBlocking {
            repository.getNeoExtended("1")
            coVerify(exactly = 1) { neoDao.getById(any()) }
        }
    }

    @Test
    fun `test observeNeoSimple`() {
        runBlocking {
            val neos = listOf(
                DUMMY_NEO_ENTITY.copy(date = "2015-06-06"),
                DUMMY_NEO_ENTITY.copy(date = "2015-09-09"),
                DUMMY_NEO_ENTITY.copy(date = "2015-01-01")
            )

            every { neoDao.observe() } returns flowOf(neos)

            val flow = repository.observeNeoSimple()


            verify(exactly = 1) { neoDao.observe() }

            flow.collect { list ->
                assert(list == neos.groupBy({ it.date }, NeoEntity::toNeoSimple).toSortedMap())
            }
        }
    }

    companion object {
        private val DUMMY_NEO_ENTITY = NeoEntity(
            date = "1970-01-01",
            id = "1",
            neoReferenceId = "1",
            name = "Monster",
            nasaJplUrl = "https://example.com",
            absoluteMagnitudeH = 18.19,
            estimatedDiameter = EstimatedDiameterLocal(
                kilometers = EstimatedDiameterValuesLocal(
                    estimatedDiameterMin = 20.21,
                    estimatedDiameterMax = 22.23
                ), meters = EstimatedDiameterValuesLocal(
                    estimatedDiameterMin = 24.25,
                    estimatedDiameterMax = 26.27
                ), miles = EstimatedDiameterValuesLocal(
                    estimatedDiameterMin = 28.29,
                    estimatedDiameterMax = 30.31
                ), feet = EstimatedDiameterValuesLocal(
                    estimatedDiameterMin = 32.33,
                    estimatedDiameterMax = 34.35
                )
            ),
            isPotentiallyHazardousAsteroid = false,
            closeApproachData = listOf(),
            isSentryObject = false
        )
        private val DUMMY_RESPONSE = NearEarthObjectsResponse(
            links = Links(
                next = null,
                previous = null,
                self = "deseruisse"
            ), elementCount = 5141, nearEarthObjects = mapOf()
        )
    }
}