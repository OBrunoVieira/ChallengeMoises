package com.challenge.moises.feature.songs.domain.usecase

import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.feature.songs.domain.repository.RecentSongsRepository
import javax.inject.Inject

class SaveRecentSongUseCase @Inject constructor(
    private val repository: RecentSongsRepository
) {
    suspend operator fun invoke(song: Song) {
        repository.saveRecentSong(song)
    }
}
