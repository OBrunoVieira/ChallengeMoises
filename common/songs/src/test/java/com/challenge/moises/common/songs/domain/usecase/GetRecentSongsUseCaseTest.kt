package com.challenge.moises.common.songs.domain.usecase

import com.challenge.moises.common.songs.domain.repository.RecentSongsRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class GetRecentSongsUseCaseTest {

    private val repository = mockk<RecentSongsRepository>()
    private val useCase = GetRecentSongsUseCase(repository)

    @Test
    fun `invoke calls repository`() = runTest {
        // Given
        every { repository.getRecentSongs() } returns flowOf(emptyList())

        // When
        useCase()

        // Then
        verify { repository.getRecentSongs() }
    }
}
