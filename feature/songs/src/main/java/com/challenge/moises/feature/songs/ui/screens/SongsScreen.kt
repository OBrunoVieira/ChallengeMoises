package com.challenge.moises.feature.songs.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.design.components.MoisesCircularLoading
import com.challenge.moises.design.components.MoisesIconButton
import com.challenge.moises.design.components.MoisesScaffold
import com.challenge.moises.design.components.MoisesSearchTextField
import com.challenge.moises.design.components.SongListItem
import com.challenge.moises.design.tokens.MoisesSpacings
import com.challenge.moises.design.tokens.annotations.MoisesPreviewScreenSizes
import com.challenge.moises.feature.songs.ui.components.MoreOptionsBottomSheet
import com.challenge.moises.feature.songs.ui.components.SearchPlaceholder
import com.challenge.moises.feature.songs.ui.models.states.SongsUiState
import com.challenge.moises.feature.songs.ui.viewmodels.SongsViewModel
import com.challenge.moises.design.R as DesignR

@Composable
fun SongsScreen(
    onSongClick: (String) -> Unit,
    onAlbumClick: (String) -> Unit = {},
    viewModel: SongsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()

    SongsScreen(
        uiState = uiState,
        query = query,
        onQueryChanged = viewModel::onQueryChanged,
        onClearQuery = viewModel::clearQuery,
        onSongClick = onSongClick,
        onAlbumClick = onAlbumClick,
        onRecentSongAdd = viewModel::saveRecentSong,
        onRemoveRecentSong = viewModel::removeRecentSong
    )
}

@Composable
private fun SongsScreen(
    uiState: SongsUiState,
    query: String,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () -> Unit,
    onSongClick: (String) -> Unit,
    onAlbumClick: (String) -> Unit,
    onRecentSongAdd: (Song) -> Unit,
    onRemoveRecentSong: (Song) -> Unit
) {
    var isSearchEnabled by remember { mutableStateOf(false) }
    val isSearching = query.isNotBlank()

    var selectedSongForOptions by remember { mutableStateOf<Song?>(null) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    BackHandler(isSearchEnabled) {
        onClearQuery()
        isSearchEnabled = false
    }

    LaunchedEffect(isSearchEnabled) {
        if (isSearchEnabled) {
            focusRequester.requestFocus()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            onClearQuery()
        }
    }

    MoisesScaffold(
        titleContent = {
            AnimatedContent(
                targetState = isSearchEnabled,
                transitionSpec = {
                    fadeIn() + slideInHorizontally() togetherWith fadeOut() + slideOutHorizontally()
                },
                label = "TitleAnimation"
            ) {
                if (it) {
                    MoisesSearchTextField(
                        modifier = Modifier.focusRequester(focusRequester),
                        query = query,
                        onQueryChanged = onQueryChanged,
                        onSearch = {
                            isSearchEnabled = false
                            keyboardController?.hide()
                        }
                    )
                } else {
                    Text(
                        text = stringResource(DesignR.string.songs_title),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color.White
                    )
                }
            }
        },
        actions = {
            MoisesIconButton(
                onClick = {
                    if (isSearchEnabled) onClearQuery()
                    isSearchEnabled = !isSearchEnabled
                },
                imageVector = if (isSearchEnabled) Icons.Default.Close else Icons.Default.Search,
                contentDescription = stringResource(DesignR.string.search_action_label),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(
                    vertical = MoisesSpacings.small,
                    horizontal = MoisesSpacings.large
                )
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    MoisesCircularLoading(
                        modifier = Modifier.align(Alignment.Center)
                    )
                } else {
                    if (!isSearching && uiState.recentSongs.isEmpty()) {
                        SearchPlaceholder(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(MoisesSpacings.extraSmall)
                        ) {
                            val songs =
                                if (isSearching) uiState.searchedSongs else uiState.recentSongs

                            items(songs, key = { it.id }) { song ->
                                SongListItem(
                                    title = song.title,
                                    subtitle = song.artistName,
                                    imageUrl = song.artworkUrl,
                                    hasVideo = song.kind == "music-video",
                                    isExplicit = song.isExplicit,
                                    onClick = {
                                        if (isSearching) {
                                            onRecentSongAdd(song)
                                        }
                                        onSongClick(song.id)
                                    },
                                    onMoreClick = { selectedSongForOptions = song }
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    selectedSongForOptions?.let { song ->
        MoreOptionsBottomSheet(
            song = song,
            onDismissRequest = { selectedSongForOptions = null },
            onAlbumClick = onAlbumClick,
            onRemoveFromRecent = if (!isSearching) {
                { onRemoveRecentSong(song) }
            } else {
                null
            }
        )
    }
}

@MoisesPreviewScreenSizes
@Composable
fun SongsScreenPreview() {
    SongsScreen(
        uiState = SongsUiState(
            searchedSongs = listOf(
                Song(
                    id = "1",
                    artistId = "123",
                    title = "Take On Me",
                    artistName = "a-ha",
                    albumName = "Hunting High and Low",
                    collectionId = null,
                    artworkUrl = null,
                    previewUrl = null,
                    kind = "",
                    isCollection = false
                ),
                Song(
                    id = "2",
                    artistId = "456",
                    title = "Billie Jean",
                    artistName = "Michael Jackson",
                    albumName = "Thriller",
                    collectionId = null,
                    artworkUrl = null,
                    previewUrl = null,
                    kind = "",
                    isCollection = false
                )
            )
        ),
        query = "a-ha",
        onQueryChanged = {},
        onClearQuery = {},
        onSongClick = {},
        onAlbumClick = {},
        onRecentSongAdd = {},
        onRemoveRecentSong = {}
    )
}