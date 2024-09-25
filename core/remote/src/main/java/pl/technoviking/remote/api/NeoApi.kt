package pl.technoviking.remote.api

import pl.technoviking.remote.api.model.NearEarthObjectsResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface NeoApi {

    @GET("/feed")
    suspend fun getNearEarthObjectsResponse(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
    ): NearEarthObjectsResponse
}