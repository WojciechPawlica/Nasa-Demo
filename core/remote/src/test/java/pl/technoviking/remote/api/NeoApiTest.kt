package pl.technoviking.remote.api

import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import pl.technoviking.remote.api.model.Links
import pl.technoviking.remote.api.model.NearEarthObjectsResponse
import pl.technoviking.remote.api.model.neo.EstimatedDiameterDto
import pl.technoviking.remote.api.model.neo.EstimatedDiameterValuesDto
import pl.technoviking.remote.api.model.neo.NeoDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class NeoApiTest {
    private lateinit var server: MockWebServer
    private lateinit var api: NeoApi

    @Before
    fun setup() {
        server = MockWebServer()
        api = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(NeoApi::class.java)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun `getNearEarthObjectsResponse, returns Success`() = runBlocking {
        val res = MockResponse()
        res.setBody(Gson().toJson(DUMMY_RESPONSE))
        server.enqueue(res)

        val neoResponse = api.getNearEarthObjectsResponse("", "")
        server.takeRequest()

        assert(neoResponse == DUMMY_RESPONSE)
    }

    companion object {
        private val DUMMY_RESPONSE = NearEarthObjectsResponse(
            links = Links(
                next = null,
                previous = null,
                self = "asd"
            ),
            elementCount = 1,
            nearEarthObjects = mapOf(
                "1970-01-01" to listOf(
                    NeoDto(
                        links = Links(
                            next = null,
                            previous = null,
                            self = "asd"
                        ),
                        id = "1",
                        neoReferenceId = "1",
                        name = "Monster",
                        nasaJplUrl = "https://example.com",
                        absoluteMagnitudeH = 54.55,
                        estimatedDiameter = EstimatedDiameterDto(
                            kilometers = EstimatedDiameterValuesDto(
                                estimatedDiameterMin = 56.57,
                                estimatedDiameterMax = 58.59
                            ), meters = EstimatedDiameterValuesDto(
                                estimatedDiameterMin = 60.61,
                                estimatedDiameterMax = 62.63
                            ), miles = EstimatedDiameterValuesDto(
                                estimatedDiameterMin = 64.65,
                                estimatedDiameterMax = 66.67
                            ), feet = EstimatedDiameterValuesDto(
                                estimatedDiameterMin = 68.69,
                                estimatedDiameterMax = 70.71
                            )
                        ),
                        isPotentiallyHazardousAsteroid = false,
                        closeApproachData = listOf(),
                        isSentryObject = false
                    )
                )
            )
        )
    }
}