package com.challenge.moises.common.songs.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.challenge.moises.core.network.domain.models.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File
import java.nio.file.Files

@OptIn(ExperimentalCoroutinesApi::class)
class RecentSongsRepositoryImplTest {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: RecentSongsRepositoryImpl
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)
    private val tempFolder = Files.createTempDirectory("test_datastore").toFile()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        dataStore = PreferenceDataStoreFactory.create(
            scope = testScope,
            produceFile = { File(tempFolder, "test.preferences_pb") }
        )
        repository = RecentSongsRepositoryImpl(dataStore)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        tempFolder.deleteRecursively()
    }

    @Test
    fun `getRecentSongs returns correct list`() = runTest {
        // Given
        val songs = listOf(
            Song(id = "1", isCollection = false, artistId = "A1", title = "T1", artistName = "AN1", albumName = "AL1", collectionId = "C1", artworkUrl = null, previewUrl = null)
        )
        repository.saveRecentSong(songs[0])

        // When
        val result = repository.getRecentSongs().first()

        // Then
        assertEquals(1, result.size)
        assertEquals("1", result[0].id)
    }

    @Test
    fun `saveRecentSong adds song to the top and removes duplicates`() = runTest {
        // Given
        val song1 = Song(id = "1", isCollection = false, artistId = "A1", title = "T1", artistName = "AN1", albumName = "AL1", collectionId = "C1", artworkUrl = null, previewUrl = null)
        val song2 = Song(id = "2", isCollection = false, artistId = "A2", title = "T2", artistName = "AN2", albumName = "AL2", collectionId = "C2", artworkUrl = null, previewUrl = null)
        
        repository.saveRecentSong(song1)
        repository.saveRecentSong(song2)
        repository.saveRecentSong(song1) // Move 1 to top

        // When
        val result = repository.getRecentSongs().first()

        // Then
        assertEquals(2, result.size)
        assertEquals("1", result[0].id)
        assertEquals("2", result[1].id)
    }
}