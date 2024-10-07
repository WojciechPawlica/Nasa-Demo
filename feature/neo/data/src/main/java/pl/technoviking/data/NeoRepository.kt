package pl.technoviking.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import pl.technoviking.common.di.Default
import pl.technoviking.common.di.IoDispatcher
import pl.technoviking.data.model.NeoExtended
import pl.technoviking.data.model.NeoSimple
import pl.technoviking.data.model.mapper.toLocal
import pl.technoviking.data.model.mapper.toNeoExtended
import pl.technoviking.data.model.mapper.toNeoSimple
import pl.technoviking.local.neo.NeoDao
import pl.technoviking.local.neo.NeoEntity
import pl.technoviking.remote.api.NeoApi
import retrofit2.HttpException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

interface NeoRepository {
    suspend fun fetchData(startDate: LocalDate, endDate: LocalDate)
    fun fetchDataFlow(startDate: LocalDate, endDate: LocalDate) : Flow<Boolean>
    fun observeNeoSimple(): Flow<Map<String, List<NeoSimple>>>
    suspend fun getNeoExtended(id: String): NeoExtended
}

class NeoRepositoryImpl @Inject constructor(
    private val localDataSource: NeoDao,
    private val remoteDataSource: NeoApi,
    @Default private val dateTimeFormatter: DateTimeFormatter,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : NeoRepository {

    override suspend fun fetchData(startDate: LocalDate, endDate: LocalDate) {
        withContext(ioDispatcher) {
            runCatching {
                val response = remoteDataSource.getNearEarthObjectsResponse(
                    startDate.format(dateTimeFormatter),
                    endDate.format(dateTimeFormatter)
                )
                localDataSource.clear()
                localDataSource.upsert(response.toLocal())
            }.onFailure {
                /**
                 * Temporary solution just to get a detailed error body
                 * TODO: Handle errors in CallAdapter
                 */
                throw if (it is HttpException) {
                    Exception(it.response()?.errorBody()?.string())
                } else {
                    it
                }
            }
        }
    }

    override fun fetchDataFlow(startDate: LocalDate, endDate: LocalDate): Flow<Boolean> {
        return flow {
            val response = remoteDataSource.getNearEarthObjectsResponse(
                startDate.format(dateTimeFormatter),
                endDate.format(dateTimeFormatter)
            )
            emit(response)
        }.onEach { response ->
            localDataSource.clear()
            localDataSource.upsert(response.toLocal())
        }.map {
            true
        }.catch {
            emit(false)
        }
    }

    override fun observeNeoSimple(): Flow<Map<String, List<NeoSimple>>> =
        localDataSource.observe()
            .map { list -> list.groupBy({ it.date }, NeoEntity::toNeoSimple).toSortedMap() }

    override suspend fun getNeoExtended(id: String): NeoExtended = withContext(ioDispatcher) {
        localDataSource.getById(id).toNeoExtended()
    }
}