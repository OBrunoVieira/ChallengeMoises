package com.challenge.moises.feature.songs.ui.models.states

import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.design.ui.models.MoisesErrorType

data class SongsUiState(
    val isLoading: Boolean = false,
    val recentPlayedSongs: List<Song> = emptyList(),
    val errorType: MoisesErrorType? = null
)
