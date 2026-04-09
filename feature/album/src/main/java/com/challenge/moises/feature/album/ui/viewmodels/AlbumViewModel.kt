package com.challenge.moises.feature.album.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.moises.design.ui.models.MoisesErrorType
import com.challenge.moises.feature.album.domain.usecase.GetAlbumSongsUseCase
import com.challenge.moises.feature.album.ui.models.states.AlbumUiState
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

@HiltViewModel(assistedFactory = AlbumViewModel.Factory::class)
class AlbumViewModel @AssistedInject constructor(
    private val getAlbumSongsUseCase: GetAlbumSongsUseCase,
    @Assisted private val albumId: String
) : ViewModel() {

    private val _uiState = MutableStateFlow(AlbumUiState())
    val uiState: StateFlow<AlbumUiState> = _uiState.asStateFlow()

    init {
        loadAlbum(albumId)
    }

    @AssistedFactory
    interface Factory {
        fun create(albumId: String): AlbumViewModel
    }

    fun loadAlbum(albumId: String) {
        viewModelScope.launch {
            getAlbumSongsUseCase(albumId)
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
                    val (collections, songs) = songs.partition { it.isCollection }
                    _uiState.update { it.copy(isLoading = false, album = collections.firstOrNull(), songs = songs) }
                }
        }
    }
}
