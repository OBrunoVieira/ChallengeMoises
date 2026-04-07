package com.challenge.moises.feature.song_details.domain.usecase

import com.challenge.moises.core.network.data.repositories.ItunesRepository
import com.challenge.moises.core.network.domain.models.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSongDetailsUseCase @Inject constructor(
    private val repository: ItunesRepository
) {
    operator fun invoke(trackId: String): Flow<List<Song>> = repository.getSongDetails(trackId)
}
