package pl.technoviking.local.neo

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pl.technoviking.local.LocalDatabase
import pl.technoviking.local.neo.model.EstimatedDiameterLocal
import pl.technoviking.local.neo.model.EstimatedDiameterValuesLocal

@RunWith(AndroidJUnit4::class)
class NeoDaoTest {

    private lateinit var db: LocalDatabase
    private lateinit var neoDao: NeoDao

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            LocalDatabase::class.java
        ).build()
        neoDao = db.neoDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun writeAndObserve() {
        runBlocking {
            val entities = listOf(
                DUMMY_NEO_ENTITY,
                DUMMY_NEO_ENTITY.copy(id = "2"),
                DUMMY_NEO_ENTITY.copy(id = "3")
            )
            neoDao.upsert(entities)

            val neos = neoDao.observe().first()
            assert(neos.size == 3)
            assert(neos[0] == DUMMY_NEO_ENTITY)
        }
    }

    @Test
    fun writeAndRead() {
        runBlocking {
            val entities = listOf(DUMMY_NEO_ENTITY)
            neoDao.upsert(entities)

            val neo = neoDao.getById(DUMMY_NEO_ENTITY.id)
            assert(neo == DUMMY_NEO_ENTITY)
        }
    }

    @Test
    fun writeAndClear() {
        runBlocking {
            val entities = listOf(
                DUMMY_NEO_ENTITY,
                DUMMY_NEO_ENTITY.copy(id = "2"),
                DUMMY_NEO_ENTITY.copy(id = "3")
            )
            neoDao.upsert(entities)

            var neos = neoDao.observe().first()
            assert(neos.size == 3)

            neoDao.clear()

            neos = neoDao.observe().first()
            assert(neos.isEmpty())
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
    }
}