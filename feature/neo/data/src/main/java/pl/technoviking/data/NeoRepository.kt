package pl.technoviking.data

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
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
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

interface NeoRepository {
    suspend fun fetchData(startDate: LocalDate, endDate: LocalDate)
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
            val response = remoteDataSource.getNearEarthObjectsResponse(
                startDate.format(dateTimeFormatter),
                endDate.format(dateTimeFormatter)
            )
            localDataSource.clear()
            localDataSource.upsert(response.toLocal())
        }
    }

    override fun observeNeoSimple(): Flow<Map<String, List<NeoSimple>>> =
        localDataSource.observe().map { list -> list.groupBy({ it.date }, NeoEntity::toNeoSimple) }

    override suspend fun getNeoExtended(id: String) : NeoExtended = withContext(ioDispatcher) {
        localDataSource.getById(id).toNeoExtended()
    }
}