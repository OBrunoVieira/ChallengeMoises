package com.challenge.moises.common.songs.domain.usecase

import com.challenge.moises.common.songs.domain.repository.RecentSongsRepository
import com.challenge.moises.core.network.domain.models.Song
import javax.inject.Inject

class RemoveRecentSongUseCase @Inject constructor(
    private val repository: RecentSongsRepository
) {
    suspend operator fun invoke(song: Song) {
        repository.removeRecentSong(song)
    }
}
