package com.challenge.moises.common.songs.domain.usecase

import com.challenge.moises.common.songs.domain.repository.RecentSongsRepository
import com.challenge.moises.core.network.domain.models.Song
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class RemoveRecentSongUseCaseTest {

    private val repository = mockk<RecentSongsRepository>()
    private val useCase = RemoveRecentSongUseCase(repository)

    @Test
    fun `invoke calls repository`() = runTest {
        // Given
        val song = mockk<Song>()
        coEvery { repository.removeRecentSong(song) } returns Unit

        // When
        useCase(song)

        // Then
        coVerify { repository.removeRecentSong(song) }
    }
}
