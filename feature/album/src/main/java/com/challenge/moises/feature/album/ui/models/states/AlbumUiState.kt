package com.challenge.moises.feature.album.ui.models.states

import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.design.ui.models.MoisesErrorType

data class AlbumUiState(
    val isLoading: Boolean = false,
    val album: Song? = null,
    val songs: List<Song> = emptyList(),
    val errorType: MoisesErrorType? = null
)
