package com.challenge.moises.feature.song_details.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.design.components.MoisesCircularLoading
import com.challenge.moises.design.components.MoisesIconButton
import com.challenge.moises.design.components.MoisesSlider
import com.challenge.moises.design.components.SongListItem
import com.challenge.moises.design.tokens.MoisesSpacings
import com.challenge.moises.design.tokens.annotations.MoisesPreviewScreenSizes
import com.challenge.moises.feature.song_details.ui.models.states.SongDetailsUiState
import com.challenge.moises.feature.song_details.ui.viewmodels.SongDetailsViewModel
import kotlinx.coroutines.delay
import com.challenge.moises.design.R as DesignR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SongDetailsScreen(
    songId: String,
    onBackClick: () -> Unit,
    onAlbumClick: (String) -> Unit,
    viewModel: SongDetailsViewModel = hiltViewModel<SongDetailsViewModel, SongDetailsViewModel.Factory>(
        key = songId,
        creationCallback = { factory -> factory.create(songId) }
    )
) {
    val uiState by viewModel.uiState.collectAsState()

    val context = LocalContext.current
    val player = remember { ExoPlayer.Builder(context).build() }

    var currentPosition by remember { mutableLongStateOf(0L) }
    var duration by remember { mutableLongStateOf(0L) }

    LaunchedEffect(player, uiState.isPlaying) {
        if (uiState.isPlaying) {
            while (true) {
                currentPosition = player.currentPosition
                duration = player.duration.coerceAtLeast(0L)
                delay(100)
            }
        }
    }

    DisposableEffect(player) {
        val listener = object : Player.Listener {
            override fun onIsPlayingChanged(isPlayingParam: Boolean) {
                viewModel.onIsPlayingChanged(isPlayingParam)
            }
        }

        player.addListener(listener)
        onDispose {
            player.removeListener(listener)
            player.release()
        }
    }

    val song = uiState.song
    LaunchedEffect(song?.previewUrl) {
        song?.previewUrl?.let { url ->
            player.setMediaItem(MediaItem.fromUri(url))
            player.prepare()
        }
    }

    SongDetailsScreen(
        uiState = uiState,
        currentPosition = currentPosition,
        duration = duration,
        onBackClick = onBackClick,
        onAlbumClick = onAlbumClick,
        onPlay = { player.play() },
        onPause = { player.pause() },
        onSeek = { position ->
            player.seekTo(position)

            //It synchronizes the music timeline position with the seek position.
            currentPosition = position
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SongDetailsScreen(
    uiState: SongDetailsUiState,
    currentPosition: Long = 0L,
    duration: Long = 0L,
    onBackClick: () -> Unit = {},
    onAlbumClick: (String) -> Unit = { "" },
    onPlay: () -> Unit = {},
    onPause: () -> Unit = {},
    onSeek: (Long) -> Unit = {},
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(DesignR.string.song_details_title)) },
                navigationIcon = {
                    MoisesIconButton(
                        onClick = onBackClick,
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(DesignR.string.back_content_description)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        },
        containerColor = Color.Black
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            if (uiState.isLoading) {
                MoisesCircularLoading(
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            //TODO - Optmize error handling
            AnimatedVisibility(
                visible = !uiState.isLoading && uiState.errorMessage != null,
                enter = fadeIn(), exit = fadeOut(),
                modifier = Modifier.align(Alignment.Center)
            ) {
                Text(
                    text = uiState.errorMessage ?: "Unknown error",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(MoisesSpacings.medium)
                )
            }

            AnimatedVisibility(
                visible = !uiState.isLoading && uiState.song != null,
                enter = fadeIn(), exit = fadeOut()
            ) {
                MoisesPlayerControl(
                    uiState.song,
                    uiState.isPlaying,
                    onPause,
                    onAlbumClick,
                    currentPosition,
                    duration,
                    onSeek,
                    onPlay
                )
            }
        }
    }
}

@Composable
private fun MoisesPlayerControl(
    song: Song?,
    isPlaying: Boolean,
    onPause: () -> Unit,
    onAlbumClick: (String) -> Unit,
    currentPosition: Long,
    duration: Long,
    onSeek: (Long) -> Unit,
    onPlay: () -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(MoisesSpacings.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(MoisesSpacings.medium)
    ) {
        song?.albumName?.let { albumName ->
            Text(
                text = stringResource(DesignR.string.album_label, albumName),
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable {
                        song.collectionId?.let {
                            onPause()
                            onAlbumClick(it)
                        }
                    }
            )
        }

        SongListItem(
            title = song?.title.orEmpty(),
            subtitle = song?.artistName.orEmpty(),
            imageUrl = song?.artworkUrl,
            trailingIcon = null
        )

        song?.previewUrl?.let {
            MoisesSlider(
                currentPosition = currentPosition,
                duration = duration,
                onSeek = onSeek
            )

            MoisesIconButton(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) "Pause" else "Play",
                onClick = {
                    if (isPlaying) {
                        onPause()
                    } else {
                        onPlay()
                    }
                },
                iconSize = 64.dp,
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@MoisesPreviewScreenSizes
@Composable
fun SongDetailsScreenPreview() {
    SongDetailsScreen(
        uiState = SongDetailsUiState(
            song = Song(
                id = "1",
                title = "Take On Me",
                artistName = "a-ha",
                albumName = "Hunting High and Low",
                artworkUrl = null,
                previewUrl = "https://example.com/preview.mp3",
                collectionId = "123",
                artistId = "456"
            )
        )
    )
}
