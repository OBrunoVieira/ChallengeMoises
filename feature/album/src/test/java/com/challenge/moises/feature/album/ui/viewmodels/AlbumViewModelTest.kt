package com.challenge.moises.feature.album.ui.viewmodels

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.design.ui.models.MoisesErrorType
import com.challenge.moises.feature.album.domain.usecase.GetAlbumSongsUseCase
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
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AlbumViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getAlbumSongsUseCase = mockk<GetAlbumSongsUseCase>()
    private lateinit var viewModel: AlbumViewModel
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
    fun `loadAlbum success updates uiState with album and songs`() = runTest {
        // Given
        val albumId = "123"
        val album = Song(id = "123", isCollection = true, artistId = "Artist1", title = "Album 1", artistName = "Artist 1", albumName = "Album 1", collectionId = "123", artworkUrl = null, previewUrl = null)
        val songs = listOf(
            album,
            Song(id = "1", isCollection = false, artistId = "Artist1", title = "Song 1", artistName = "Artist 1", albumName = "Album 1", collectionId = "123", artworkUrl = null, previewUrl = null),
            Song(id = "2", isCollection = false, artistId = "Artist1", title = "Song 2", artistName = "Artist 1", albumName = "Album 1", collectionId = "123", artworkUrl = null, previewUrl = null)
        )
        every { getAlbumSongsUseCase(albumId) } returns flowOf(songs)

        // When
        viewModel = AlbumViewModel(getAlbumSongsUseCase, albumId)

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(album, viewModel.uiState.value.album)
        assertEquals(2, viewModel.uiState.value.songs.size)
        assertEquals("Song 1", viewModel.uiState.value.songs[0].title)
    }

    @Test
    fun `loadAlbum error updates uiState with error type`() = runTest {
        // Given
        val albumId = "123"
        val exception = java.io.IOException("Network Error")
        every { getAlbumSongsUseCase(albumId) } returns flow { throw exception }

        // When
        viewModel = AlbumViewModel(getAlbumSongsUseCase, albumId)

        // Then
        assertFalse(viewModel.uiState.value.isLoading)
        assertEquals(MoisesErrorType.INTERNET, viewModel.uiState.value.errorType)
    }
}
