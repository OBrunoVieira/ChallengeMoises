package com.challenge.moises.feature.songs.domain.usecase

import com.challenge.moises.core.network.data.repositories.ItunesRepository
import com.challenge.moises.core.network.domain.models.Song
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class SearchSongsUseCase @Inject constructor(
    private val repository: ItunesRepository
) {
    operator fun invoke(query: String): Flow<List<Song>> {
        if (query.isBlank()) return emptyFlow()
        return repository.searchSongs(query)
    }
}
