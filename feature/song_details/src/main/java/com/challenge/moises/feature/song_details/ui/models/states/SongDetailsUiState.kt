package com.challenge.moises.feature.song_details.ui.models.states

import com.challenge.moises.core.network.domain.models.Song

data class SongDetailsUiState(
    val isLoading: Boolean = false,
    val isPlaying:Boolean = false,
    val song: Song? = null,
    val errorMessage: String? = null
)
