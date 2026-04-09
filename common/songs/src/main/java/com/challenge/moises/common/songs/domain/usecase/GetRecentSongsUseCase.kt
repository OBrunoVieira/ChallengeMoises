package com.challenge.moises.common.songs.domain.usecase

import com.challenge.moises.common.songs.domain.repository.RecentSongsRepository
import javax.inject.Inject

class GetRecentSongsUseCase @Inject constructor(
    private val repository: RecentSongsRepository
) {
    operator fun invoke() = repository.getRecentSongs()
}
