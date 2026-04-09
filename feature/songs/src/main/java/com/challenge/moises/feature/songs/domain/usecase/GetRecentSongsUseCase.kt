package com.challenge.moises.feature.songs.domain.usecase

import com.challenge.moises.feature.songs.data.repository.RecentSongsRepository
import javax.inject.Inject

class GetRecentSongsUseCase @Inject constructor(
    private val repository: RecentSongsRepository
) {
    operator fun invoke() = repository.getRecentSongs()
}
