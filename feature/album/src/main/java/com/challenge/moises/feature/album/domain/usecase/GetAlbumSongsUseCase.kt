package com.challenge.moises.feature.album.domain.usecase

import com.challenge.moises.core.network.data.repositories.ItunesRepository
import com.challenge.moises.core.network.domain.models.Song
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAlbumSongsUseCase @Inject constructor(
    private val repository: ItunesRepository
) {
    operator fun invoke(albumId: String): Flow<List<Song>> {
        return repository.getAlbumSongs(albumId)
    }
}
