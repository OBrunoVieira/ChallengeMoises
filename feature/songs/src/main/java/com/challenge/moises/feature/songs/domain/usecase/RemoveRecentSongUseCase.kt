package com.challenge.moises.feature.songs.domain.usecase

import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.feature.songs.data.repository.RecentSongsRepository
import javax.inject.Inject

class RemoveRecentSongUseCase @Inject constructor(
    private val repository: RecentSongsRepository
) {
    suspend operator fun invoke(song: Song) {
        repository.removeRecentSong(song)
    }
}
