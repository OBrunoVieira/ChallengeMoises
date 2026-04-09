package com.challenge.moises.feature.songs.domain.usecase

import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.feature.songs.data.repository.RecentSongsRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SaveRecentSongUseCaseTest {

    private val repository = mockk<RecentSongsRepository>()
    private val useCase = SaveRecentSongUseCase(repository)

    @Test
    fun `invoke calls repository`() = runTest {
        // Given
        val song = mockk<Song>()
        coEvery { repository.saveRecentSong(song) } returns Unit

        // When
        useCase(song)

        // Then
        coVerify { repository.saveRecentSong(song) }
    }
}
