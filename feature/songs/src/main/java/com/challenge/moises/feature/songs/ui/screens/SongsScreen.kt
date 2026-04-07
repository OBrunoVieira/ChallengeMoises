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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.ImeAction
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.design.components.MoisesCircularLoading
import com.challenge.moises.design.components.MoisesIconButton
import com.challenge.moises.design.components.SongListItem
import com.challenge.moises.design.tokens.MoisesSpacings
import com.challenge.moises.design.tokens.annotations.MoisesPreviewScreenSizes
import com.challenge.moises.feature.songs.ui.components.SearchPlaceholder
import com.challenge.moises.feature.songs.ui.models.states.SongsUiState
import com.challenge.moises.feature.songs.ui.viewmodels.SongsViewModel
import com.challenge.moises.design.R as DesignR

@Composable
fun SongsScreen(
    onSongClick: (String) -> Unit,
    viewModel: SongsViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()

    SongsScreen(
        uiState = uiState,
        query = query,
        onQueryChanged = viewModel::onQueryChanged,
        onClearQuery = viewModel::clearQuery,
        onSongClick = onSongClick
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongsScreen(
    uiState: SongsUiState,
    query: String,
    onQueryChanged: (String) -> Unit,
    onClearQuery: () -> Unit,
    onSongClick: (String) -> Unit
) {
    var isSearching by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    BackHandler(isSearching) {
        isSearching = false
    }

    LaunchedEffect(isSearching) {
        if (isSearching) {
            focusRequester.requestFocus()
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    AnimatedContent(
                        targetState = isSearching,
                        transitionSpec = {
                            fadeIn() + slideInHorizontally() togetherWith fadeOut() + slideOutHorizontally()
                        },
                        label = "TitleAnimation"
                    ) { searching ->
                        if (searching) {
                            TextField(
                                value = query,
                                onValueChange = onQueryChanged,
                                placeholder = {
                                    Text(
                                        stringResource(DesignR.string.search_hint),
                                        color = Color.Gray
                                    )
                                },
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.Transparent,
                                    unfocusedContainerColor = Color.Transparent,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    cursorColor = Color.White,
                                    focusedTextColor = Color.White
                                ),
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .focusRequester(focusRequester),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        isSearching = false
                                        keyboardController?.hide()
                                    }
                                )
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
                            if(isSearching) onClearQuery()
                            isSearching = true
                        },
                        imageVector = if (isSearching) Icons.Default.Close else Icons.Default.Search,
                        contentDescription = stringResource(DesignR.string.search_action_label),
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                ),
                modifier = Modifier.statusBarsPadding()
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = MoisesSpacings.large)
        ) {
            Box(modifier = Modifier.fillMaxSize()) {
                if (uiState.isLoading) {
                    MoisesCircularLoading(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                if (!uiState.isLoading) {
                    if (query.isBlank() && uiState.songs.isEmpty()) {
                        SearchPlaceholder(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = MoisesSpacings.extraLarge),
                            verticalArrangement = Arrangement.spacedBy(MoisesSpacings.extraSmall)
                        ) {
                            items(uiState.songs, key = { it.id }) { song ->
                                SongListItem(
                                    title = song.title,
                                    subtitle = song.artistName,
                                    imageUrl = song.artworkUrl,
                                    onClick = { onSongClick(song.id) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@MoisesPreviewScreenSizes
@Composable
fun SongsScreenPreview() {
    SongsScreen(
        uiState = SongsUiState(
            songs = listOf(
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
        onSongClick = {}
    )
}