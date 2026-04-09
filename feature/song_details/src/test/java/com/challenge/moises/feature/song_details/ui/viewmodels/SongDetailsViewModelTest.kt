package com.challenge.moises.feature.song_details.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.moises.common.songs.domain.usecase.SaveRecentSongUseCase
import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.design.ui.models.MoisesErrorType
import com.challenge.moises.feature.song_details.domain.usecase.GetSongDetailsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SongDetailsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getSongDetailsUseCase = mockk<GetSongDetailsUseCase>()
    private val saveRecentSongUseCase = mockk<SaveRecentSongUseCase>()
    private lateinit var viewModel: SongDetailsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadSong success updates uiState with song`() = runTest {
        // Given
        val songId = "123"
        val song = Song(id = "123", isCollection = false, artistId = "Artist1", title = "Song 1", artistName = "Artist 1", albumName = "Album 1", collectionId = "123", artworkUrl = null, previewUrl = null)
        every { getSongDetailsUseCase(songId) } returns flowOf(listOf(song))

        // When
        viewModel = SongDetailsViewModel(getSongDetailsUseCase, saveRecentSongUseCase, songId)

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(song, viewModel.uiState.value.song)
    }

    @Test
    fun `onIsPlayingChanged true saves song as recent`() = runTest {
        // Given
        val songId = "123"
        val song = Song(id = "123", isCollection = false, artistId = "Artist1", title = "Song 1", artistName = "Artist 1", albumName = "Album 1", collectionId = "123", artworkUrl = null, previewUrl = null)
        every { getSongDetailsUseCase(songId) } returns flowOf(listOf(song))
        coEvery { saveRecentSongUseCase(song) } returns Unit
        viewModel = SongDetailsViewModel(getSongDetailsUseCase, saveRecentSongUseCase, songId)

        // When
        viewModel.onIsPlayingChanged(true)

        // Then
        coVerify { saveRecentSongUseCase(song) }
    }

    @Test
    fun `onIsPlayingChanged updates state`() = runTest {
        // Given
        every { getSongDetailsUseCase(any()) } returns flowOf(emptyList())
        viewModel = SongDetailsViewModel(getSongDetailsUseCase, saveRecentSongUseCase, "123")

        // When
        viewModel.onIsPlayingChanged(true)

        // Then
        assertTrue(viewModel.uiState.value.isPlaying)
    }

    @Test
    fun `setPlayerReadiness updates state`() = runTest {
        // Given
        every { getSongDetailsUseCase(any()) } returns flowOf(emptyList())
        viewModel = SongDetailsViewModel(getSongDetailsUseCase, saveRecentSongUseCase, "123")

        // When
        viewModel.setPlayerReadiness(true)

        // Then
        assertTrue(viewModel.uiState.value.isPlayerReady)
    }

    @Test
    fun `loadSong error updates uiState with error type`() = runTest {
        // Given
        val songId = "123"
        val exception = java.io.IOException("Network Error")
        every { getSongDetailsUseCase(songId) } returns flow { throw exception }

        // When
        viewModel = SongDetailsViewModel(getSongDetailsUseCase, saveRecentSongUseCase, songId)

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(MoisesErrorType.INTERNET, viewModel.uiState.value.errorType)
    }
}
