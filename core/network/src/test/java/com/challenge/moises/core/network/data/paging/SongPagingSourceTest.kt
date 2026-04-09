package com.challenge.moises.core.network.data.paging

import androidx.paging.PagingSource
import com.challenge.moises.core.network.ITunesApi
import com.challenge.moises.core.network.models.ItunesSearchResponseDTO
import com.challenge.moises.core.network.models.ItunesTrackDTO
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SongPagingSourceTest {

    private val api = mockk<ITunesApi>()
    private val query = "test"
    private val pagingSource = SongPagingSource(api, query)

    @Test
    fun `load returns success when api returns results`() = runTest {
        // Given
        val tracks = listOf(
            ItunesTrackDTO(
                wrapperType = "track",
                kind = "song",
                artistId = 1L,
                collectionId = 1L,
                trackId = 1L,
                artistName = "Artist",
                collectionName = "Album",
                trackName = "Track",
                previewUrl = "preview",
                artworkUrl100 = "artwork",
                trackExplicitness = "explicit"
            )
        )
        val response = ItunesSearchResponseDTO(1, tracks)
        coEvery { api.search(term = query, limit = any()) } returns response

        // When
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        // Then
        assertTrue(result is PagingSource.LoadResult.Page)
        val page = result as PagingSource.LoadResult.Page
        assertEquals(1, page.data.size)
        assertEquals("1", page.data[0].id)
    }

    @Test
    fun `load returns error when api throws exception`() = runTest {
        // Given
        val exception = Exception("Network error")
        coEvery { api.search(term = query, limit = any()) } throws exception

        // When
        val result = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        // Then
        assertTrue(result is PagingSource.LoadResult.Error)
        assertEquals(exception, (result as PagingSource.LoadResult.Error).throwable)
    }
}
