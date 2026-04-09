package com.challenge.moises.feature.album.domain.usecase

import com.challenge.moises.core.network.data.repositories.ItunesRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetAlbumSongsUseCaseTest {

    private val repository = mockk<ItunesRepository>()
    private val useCase = GetAlbumSongsUseCase(repository)

    @Test
    fun `invoke calls repository`() = runTest {
        // Given
        val albumId = "123"
        every { repository.getAlbumSongs(albumId) } returns flowOf(emptyList())

        // When
        useCase(albumId)

        // Then
        verify { repository.getAlbumSongs(albumId) }
    }
}
