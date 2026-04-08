package com.challenge.moises.feature.songs.ui.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.feature.songs.domain.usecase.GetRecentSongsUseCase
import com.challenge.moises.feature.songs.domain.usecase.RemoveRecentSongUseCase
import com.challenge.moises.feature.songs.domain.usecase.SaveRecentSongUseCase
import com.challenge.moises.feature.songs.domain.usecase.SearchSongsUseCase
import com.challenge.moises.feature.songs.ui.models.states.SongsUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class SongsViewModel @Inject constructor(
    private val searchSongsUseCase: SearchSongsUseCase,
    private val getRecentSongsUseCase: GetRecentSongsUseCase,
    private val saveRecentSongUseCase: SaveRecentSongUseCase,
    private val removeRecentSongUseCase: RemoveRecentSongUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SongsUiState())
    val uiState: StateFlow<SongsUiState> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    init {
        viewModelScope.launch {
            getRecentSongsUseCase().collect { recentSongs ->
                _uiState.update { it.copy(recentSongs = recentSongs) }
            }
        }

        viewModelScope.launch {
            _query
                .debounce(400)
                .distinctUntilChanged()
                .filter { it.isNotBlank() }
                .flatMapLatest { query ->
                    searchSongsUseCase(query)
                        .onStart {
                            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                        }
                        .catch { error ->
                            _uiState.update { it.copy(isLoading = false, errorMessage = error.message ?: "Unknown error") }
                        }
                }
                .collect { songs ->
                    _uiState.update { it.copy(isLoading = false, searchedSongs = songs) }
                }
        }
    }

    fun onQueryChanged(query: String) {
        _query.value = query
        if (query.isBlank()) {
            _uiState.update { it.copy(searchedSongs = emptyList(), errorMessage = null) }
        }
    }

    fun clearQuery() {
        onQueryChanged("")
    }

    fun saveRecentSong(song: Song) {
        viewModelScope.launch {
            saveRecentSongUseCase(song)
        }
    }

    fun removeRecentSong(song: Song) {
        viewModelScope.launch {
            removeRecentSongUseCase(song)
        }
    }
}
