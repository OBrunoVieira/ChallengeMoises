package com.challenge.moises.feature.album.ui.models.states

import com.challenge.moises.core.network.domain.models.Song

data class AlbumUiState(
    val isLoading: Boolean = false,
    val songs: List<Song> = emptyList(),
    val errorMessage: String? = null
)
