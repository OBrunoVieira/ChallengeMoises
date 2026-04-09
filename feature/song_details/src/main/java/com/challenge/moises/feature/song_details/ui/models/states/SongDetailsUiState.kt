package com.challenge.moises.feature.song_details.ui.models.states

import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.design.ui.models.MoisesErrorType

data class SongDetailsUiState(
    val isLoading: Boolean = false,
    val isPlaying: Boolean = false,
    val isPlayerReady: Boolean = false,
    val song: Song? = null,
    val errorType: MoisesErrorType? = null
) {
    val isPlayerLoading = isLoading || song != null && !isPlayerReady
}
