package com.challenge.moises.feature.album.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.design.components.CircledImageIcon
import com.challenge.moises.design.components.MoisesCircularLoading
import com.challenge.moises.design.components.MoisesScaffold
import com.challenge.moises.design.components.SongListItem
import com.challenge.moises.design.tokens.MoisesSpacings
import com.challenge.moises.design.tokens.annotations.MoisesPreviewScreenSizes
import com.challenge.moises.feature.album.ui.models.states.AlbumUiState
import com.challenge.moises.feature.album.ui.viewmodels.AlbumViewModel
import com.challenge.moises.design.R as DesignR

@Composable
fun AlbumScreen(
    albumId: String,
    onBackClick: () -> Unit,
    onSongClick: (String) -> Unit,
    viewModel: AlbumViewModel = hiltViewModel<AlbumViewModel, AlbumViewModel.Factory>(
        key = albumId,
        creationCallback = { factory -> factory.create(albumId) }
    )
) {
    val uiState by viewModel.uiState.collectAsState()
    
    AlbumScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onSongClick = onSongClick
    )
}

@Composable
private fun AlbumScreen(
    uiState: AlbumUiState,
    onBackClick: () -> Unit,
    onSongClick: (String) -> Unit
) {
    MoisesScaffold(
        title = stringResource(DesignR.string.album_screen_title),
        onBackClick = onBackClick,
        topBarContainerColor = Color.Black
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = MoisesSpacings.large)
        ) {
            if (uiState.isLoading) {
                MoisesCircularLoading(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            AnimatedVisibility(
                visible = !uiState.isLoading && uiState.errorMessage != null,
                enter = fadeIn(), exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = uiState.errorMessage ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(MoisesSpacings.medium),
                    textAlign = TextAlign.Center
                )
            }

            AnimatedVisibility(
                visible = !uiState.isLoading && uiState.songs.isNotEmpty(),
                enter = fadeIn(), exit = fadeOut(),
                modifier = Modifier.fillMaxSize()
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(bottom = MoisesSpacings.extraLarge),
                ) {
                    item {
                        AlbumHeader(album = uiState.album)
                        Spacer(Modifier.height(48.dp))
                    }

                    items(uiState.songs, key = { it.id }) { song ->
                        SongListItem(
                            title = song.title,
                            subtitle = song.artistName,
                            imageEnabled = false,
                            hasVideo = song.hasVideo,
                            isExplicit = song.isExplicit,
                            onClick = { onSongClick(song.id) },
                            trailingIcon = null
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun AlbumHeader(album: Song?) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = album?.albumName ?: stringResource(DesignR.string.unknown_album),
            style = MaterialTheme.typography.titleLarge,
            color = Color.White,
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CircledImageIcon(
                imageUrl = album?.artworkUrl,
                size = 18.dp
            )

            Text(
                text = album?.artistName ?: stringResource(DesignR.string.unknown_artist),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@MoisesPreviewScreenSizes
@Composable
private fun AlbumScreenPreview() {
    AlbumScreen(
        uiState = AlbumUiState(
            isLoading = false,
            album = Song(
                id = "0",
                artistId = "123",
                title = "Unknown",
                artistName = "Michael Jackson",
                albumName = "Thriller",
                collectionId = "456",
                artworkUrl = null,
                previewUrl = null,
                isCollection = true
            ),
            songs = listOf(
                Song(
                    id = "1",
                    artistId = "123",
                    title = "Wanna Be Startin' Somethin'",
                    artistName = "Michael Jackson",
                    albumName = "Thriller",
                    collectionId = "456",
                    artworkUrl = null,
                    previewUrl = null,
                    isCollection = false
                ),
                Song(
                    id = "2",
                    artistId = "123",
                    title = "Billie Jean",
                    artistName = "Michael Jackson",
                    albumName = "Thriller",
                    collectionId = "456",
                    artworkUrl = null,
                    previewUrl = null,
                    isCollection = false
                )
            ),
            errorMessage = null
        ),
        onBackClick = {},
        onSongClick = {}
    )
}
