package com.challenge.moises.feature.songs.domain.usecase

import androidx.paging.PagingData
import com.challenge.moises.core.network.data.repositories.ItunesRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SearchSongsUseCaseTest {

    private val repository = mockk<ItunesRepository>()
    private val useCase = SearchSongsUseCase(repository)

    @Test
    fun `invoke with blank query returns empty flow`() = runTest {
        // When
        val flow = useCase("")
        var emitted = false
        flow.collect { emitted = true }

        // Then
        assert(!emitted)
    }

    @Test
    fun `invoke with valid query calls repository`() {
        // Given
        val query = "Daft Punk"
        every { repository.searchSongs(query) } returns flowOf(PagingData.from(emptyList()))

        // When
        useCase(query)

        // Then
        verify { repository.searchSongs(query) }
    }
}
