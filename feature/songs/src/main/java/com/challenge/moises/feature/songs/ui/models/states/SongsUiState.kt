package com.challenge.moises.feature.songs.ui.models.states

import com.challenge.moises.core.network.domain.models.Song

data class SongsUiState(
    val isLoading: Boolean = false,
    val recentPlayedSongs: List<Song> = emptyList(),
    val errorMessage: String? = null
)
