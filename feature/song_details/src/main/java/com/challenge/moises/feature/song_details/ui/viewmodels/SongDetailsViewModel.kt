package com.challenge.moises.feature.song_details.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.moises.design.ui.models.MoisesErrorType
import com.challenge.moises.feature.song_details.domain.usecase.GetSongDetailsUseCase
import com.challenge.moises.feature.song_details.ui.models.states.SongDetailsUiState
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel(assistedFactory = SongDetailsViewModel.Factory::class)
class SongDetailsViewModel @AssistedInject constructor(
    private val getSongDetailsUseCase: GetSongDetailsUseCase,
    @Assisted private val songId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(SongDetailsUiState())
    val uiState: StateFlow<SongDetailsUiState> = _uiState.asStateFlow()

    init {
        loadSong(songId)
    }

    @AssistedFactory
    interface Factory {
        fun create(songId: String): SongDetailsViewModel
    }

    fun loadSong(songId: String) {
        viewModelScope.launch {
            getSongDetailsUseCase(songId)
                .onStart {
                    _uiState.update { it.copy(isLoading = true, errorType = null) }
                }
                .catch { error ->
                    val errorType = if (error is java.io.IOException) {
                        MoisesErrorType.INTERNET
                    } else {
                        MoisesErrorType.SERVER
                    }
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorType = errorType
                        )
                    }
                }
                .collect { songs ->
                    _uiState.update { it.copy(isLoading = false, song = songs.firstOrNull()) }
                }
        }
    }

    fun onIsPlayingChanged(isPlaying: Boolean) {
        _uiState.update { it.copy(isPlaying = isPlaying) }
    }

    fun setPlayerReadiness(isReady: Boolean) {
        _uiState.update { it.copy(isPlayerReady = isReady) }
    }
}
