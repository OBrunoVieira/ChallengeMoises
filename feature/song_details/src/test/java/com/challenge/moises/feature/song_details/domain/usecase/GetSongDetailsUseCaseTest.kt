package com.challenge.moises.feature.song_details.domain.usecase

import com.challenge.moises.core.network.data.repositories.ItunesRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetSongDetailsUseCaseTest {

    private val repository = mockk<ItunesRepository>()
    private val useCase = GetSongDetailsUseCase(repository)

    @Test
    fun `invoke calls repository`() = runTest {
        // Given
        val trackId = "123"
        every { repository.getSongDetails(trackId) } returns flowOf(emptyList())

        // When
        useCase(trackId)

        // Then
        verify { repository.getSongDetails(trackId) }
    }
}
