package com.challenge.moises.feature.album.ui.models.states

import com.challenge.moises.core.network.domain.models.Song

data class AlbumUiState(
    val isLoading: Boolean = false,
    val album: Song? = null,
    val songs: List<Song> = emptyList(),
    val errorMessage: String? = null
)
