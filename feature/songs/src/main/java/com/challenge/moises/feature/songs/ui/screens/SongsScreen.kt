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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.design.components.MoisesCircularLoading
import com.challenge.moises.design.components.MoisesError
import com.challenge.moises.design.components.MoisesIconButton
import com.challenge.moises.design.components.MoisesScaffold
import com.challenge.moises.design.components.MoisesSearchTextField
import com.challenge.moises.design.components.MoreOptionsBottomSheet
import com.challenge.moises.design.components.SongListItem
import com.challenge.moises.design.tokens.MoisesSpacings
import com.challenge.moises.design.tokens.annotations.MoisesPreviewScreenSizes
import com.challenge.moises.design.ui.models.MoisesErrorType
import com.challenge.moises.feature.songs.ui.components.SearchPlaceholder
import com.challenge.moises.feature.songs.ui.models.states.SongsUiState
import com.challenge.moises.feature.songs.ui.viewmodels.SongsViewModel
import kotlinx.coroutines.flow.flowOf
import com.challenge.moises.design.R as DesignR

@Composable
fun SongsScreen(
    onSongClick: (String) -> Unit,
    onAlbumClick: (String) -> Unit = {},
    viewModel: SongsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()
    val searchedSongs = viewModel.searchedSongs.collectAsLazyPagingItems()

    SongsScreen(
        uiState = uiState,
        query = query,
        searchedSongs = searchedSongs,
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
    searchedSongs: LazyPagingItems<Song>,
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
                        text = stringResource(DesignR.string.songs_screen_title),
                        style = MaterialTheme.typography.headlineMedium
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
                contentDescription = stringResource(DesignR.string.songs_screen_search_action_content_description),
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
                val isRefreshLoading =
                    isSearching && searchedSongs.loadState.refresh is LoadState.Loading

                val refreshError = searchedSongs.loadState.refresh as? LoadState.Error

                when {
                    refreshError != null -> {
                        val errorType = if (refreshError.error is java.io.IOException) {
                            MoisesErrorType.INTERNET
                        } else {
                            MoisesErrorType.SERVER
                        }

                        MoisesError(
                            type = errorType,
                            onRetry = { searchedSongs.retry() }
                        )
                    }

                    uiState.isLoading || isRefreshLoading -> {
                        MoisesCircularLoading(modifier = Modifier.align(Alignment.Center))
                    }

                    !isSearching && uiState.recentPlayedSongs.isEmpty() -> {
                        SearchPlaceholder(modifier = Modifier.align(Alignment.Center))
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(MoisesSpacings.extraSmall)
                        ) {
                            if (isSearching) {
                                addSearchResults(
                                    searchedSongs = searchedSongs,
                                    onItemClicked = {
                                        onSongClick(it.id)
                                    },
                                    onMoreClick = { selectedSongForOptions = it }
                                )

                                addLoading(searchedSongs = searchedSongs)
                            } else {
                                addRecentPlayedResults(
                                    recentPlayedSongs = uiState.recentPlayedSongs,
                                    onSongClick = onSongClick,
                                    onMoreClick = { selectedSongForOptions = it }
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
            title = song.title,
            subtitle = song.artistName,
            imageUrl = song.artworkUrl,
            onDismissRequest = { selectedSongForOptions = null },
            onAlbumClick = { onAlbumClick(song.collectionId.orEmpty()) },
            onRemoveFromRecent = if (!isSearching) {
                { onRemoveRecentSong(song) }
            } else {
                null
            }
        )
    }
}

private fun LazyListScope.addSearchResults(
    searchedSongs: LazyPagingItems<Song>,
    onItemClicked: (Song) -> Unit,
    onMoreClick: (Song) -> Unit
) {
    items(
        count = searchedSongs.itemCount,
        key = { index -> searchedSongs[index]?.id ?: index.toString() }
    ) { index ->
        searchedSongs[index]?.let { song ->
            SongListItem(
                title = song.title,
                subtitle = song.artistName,
                imageUrl = song.artworkUrl,
                hasVideo = song.hasVideo,
                isExplicit = song.isExplicit,
                onClick = { onItemClicked(song) },
                onMoreClick = { onMoreClick(song) }
            )
        }
    }
}

private fun LazyListScope.addRecentPlayedResults(
    recentPlayedSongs: List<Song>,
    onSongClick: (String) -> Unit,
    onMoreClick: (Song) -> Unit
) {
    items(recentPlayedSongs, key = { it.id }) { song ->
        SongListItem(
            title = song.title,
            subtitle = song.artistName,
            imageUrl = song.artworkUrl,
            hasVideo = song.hasVideo,
            isExplicit = song.isExplicit,
            onClick = { onSongClick(song.id) },
            onMoreClick = { onMoreClick(song) }
        )
    }
}

private fun LazyListScope.addLoading(searchedSongs: LazyPagingItems<Song>) {
    val state = searchedSongs.loadState.append
    if (state is LoadState.Loading) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(MoisesSpacings.medium),
                contentAlignment = Alignment.Center
            ) {
                MoisesCircularLoading()
            }
        }
    }
}

@MoisesPreviewScreenSizes
@Composable
private fun SongsScreenPreview() {
    val searchedSongs = flowOf(
        PagingData.from(
            listOf(
                Song(
                    id = "1",
                    artistId = "123",
                    title = "Take On Me",
                    artistName = "a-ha",
                    albumName = "Hunting High and Low",
                    collectionId = null,
                    artworkUrl = null,
                    previewUrl = null,
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
                    isCollection = false
                )
            )
        )
    ).collectAsLazyPagingItems()

    SongsScreen(
        uiState = SongsUiState(),
        searchedSongs = searchedSongs,
        query = "a-ha",
        onQueryChanged = {},
        onClearQuery = {},
        onSongClick = {},
        onAlbumClick = {},
        onRecentSongAdd = {},
        onRemoveRecentSong = {}
    )
}