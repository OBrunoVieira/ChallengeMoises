package com.challenge.moises.feature.songs.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.moises.common.songs.domain.usecase.GetRecentSongsUseCase
import com.challenge.moises.common.songs.domain.usecase.RemoveRecentSongUseCase
import com.challenge.moises.common.songs.domain.usecase.SaveRecentSongUseCase
import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.feature.songs.domain.usecase.SearchSongsUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SongsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val searchSongsUseCase = mockk<SearchSongsUseCase>()
    private val getRecentSongsUseCase = mockk<GetRecentSongsUseCase>()
    private val saveRecentSongUseCase = mockk<SaveRecentSongUseCase>()
    private val removeRecentSongUseCase = mockk<RemoveRecentSongUseCase>()

    private lateinit var viewModel: SongsViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { getRecentSongsUseCase() } returns flowOf(emptyList())
        viewModel = SongsViewModel(
            searchSongsUseCase,
            getRecentSongsUseCase,
            saveRecentSongUseCase,
            removeRecentSongUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `init collects recent songs`() = runTest {
        // Given
        val recentSongs = listOf(
            Song(
                id = "1",
                isCollection = false,
                artistId = "A1",
                title = "Song 1",
                artistName = "Artist 1",
                albumName = "Album 1",
                collectionId = "C1",
                artworkUrl = null,
                previewUrl = null
            )
        )
        every { getRecentSongsUseCase() } returns flowOf(recentSongs)

        // When
        viewModel = SongsViewModel(
            searchSongsUseCase,
            getRecentSongsUseCase,
            saveRecentSongUseCase,
            removeRecentSongUseCase
        )

        // Then
        assertEquals(recentSongs, viewModel.uiState.value.recentPlayedSongs)
    }

    @Test
    fun `onQueryChanged updates query state`() = runTest {
        // When
        viewModel.onQueryChanged("test")

        // Then
        assertEquals("test", viewModel.query.value)
    }

    @Test
    fun `saveRecentSong calls saveRecentSongUseCase`() = runTest {
        // Given
        val song = Song(
            id = "1",
            isCollection = false,
            artistId = "A1",
            title = "Song 1",
            artistName = "Artist 1",
            albumName = "Album 1",
            collectionId = "C1",
            artworkUrl = null,
            previewUrl = null
        )
        coEvery { saveRecentSongUseCase(song) } returns Unit

        // When
        viewModel.saveRecentSong(song)

        // Then
        coVerify { saveRecentSongUseCase(song) }
    }

    @Test
    fun `removeRecentSong calls removeRecentSongUseCase`() = runTest {
        // Given
        val song = Song(
            id = "1",
            isCollection = false,
            artistId = "A1",
            title = "Song 1",
            artistName = "Artist 1",
            albumName = "Album 1",
            collectionId = "C1",
            artworkUrl = null,
            previewUrl = null
        )
        coEvery { removeRecentSongUseCase(song) } returns Unit

        // When
        viewModel.removeRecentSong(song)

        // Then
        coVerify { removeRecentSongUseCase(song) }
    }
}
