package com.challenge.moises.feature.songs.domain.usecase

import com.challenge.moises.core.network.data.repositories.ItunesRepository
import kotlinx.coroutines.flow.emptyFlow
import javax.inject.Inject

class SearchSongsUseCase @Inject constructor(
    private val repository: ItunesRepository
) {
    operator fun invoke(query: String) = run {
        if (query.isBlank()) emptyFlow()
        else repository.searchSongs(query)
    }
}
