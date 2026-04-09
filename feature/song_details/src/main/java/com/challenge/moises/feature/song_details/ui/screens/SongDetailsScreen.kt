package com.challenge.moises.feature.song_details.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.challenge.moises.common.player.rememberMoisesPlayer
import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.design.components.MoisesCircularLoading
import com.challenge.moises.design.components.MoisesError
import com.challenge.moises.design.components.MoisesIconButton
import com.challenge.moises.design.components.MoisesScaffold
import com.challenge.moises.design.components.MoisesSlider
import com.challenge.moises.design.components.MoreOptionsBottomSheet
import com.challenge.moises.design.components.SongListItem
import com.challenge.moises.design.components.SongSize
import com.challenge.moises.design.tokens.MoisesIconSizes
import com.challenge.moises.design.tokens.MoisesSpacings
import com.challenge.moises.design.tokens.White_20
import com.challenge.moises.design.tokens.annotations.MoisesPreviewScreenSizes
import com.challenge.moises.feature.song_details.R
import com.challenge.moises.feature.song_details.ui.models.states.SongDetailsUiState
import com.challenge.moises.feature.song_details.ui.viewmodels.SongDetailsViewModel
import kotlinx.coroutines.delay
import com.challenge.moises.design.R as DesignR

@androidx.annotation.OptIn(UnstableApi::class)
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
    val player = rememberMoisesPlayer(
        onIsPlayingChanged = { isPlayingParam ->
            viewModel.onIsPlayingChanged(isPlayingParam)
        },
        onPlaybackStateChanged = { playbackState ->
            if (playbackState == Player.STATE_READY) {
                viewModel.setPlayerReadiness(true)
            }
        }
    )

    var currentPosition by rememberSaveable { mutableLongStateOf(0L) }
    var duration by rememberSaveable { mutableLongStateOf(0L) }

    LaunchedEffect(player, uiState.isPlaying) {
        if (uiState.isPlaying) {
            while (true) {
                currentPosition = player.currentPosition
                duration = player.duration.coerceAtLeast(0L)
                delay(100)
            }
        }
    }

    val song = uiState.song
    LaunchedEffect(song?.previewUrl) {
        song?.previewUrl?.let { url ->
            player.setMediaItem(MediaItem.fromUri(url))
            player.prepare()
            player.playWhenReady = true
        }
    }

    Box(Modifier.fillMaxSize()) {
        val hasVideo = uiState.song?.hasVideo == true

        if (uiState.isPlayerReady) {
            if (hasVideo) {
                AndroidView(
                    factory = { context ->
                        PlayerView(context).apply {
                            this.player = player
                            useController = false
                            resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                MoisesMusicWaveAnimation(
                    modifier = Modifier.fillMaxSize(),
                    isPlaying = uiState.isPlaying
                )
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
                currentPosition = position
            },
            onRetry = { viewModel.loadSong(songId) }
        )
    }
}

@Composable
private fun MoisesMusicWaveAnimation(
    modifier: Modifier,
    isPlaying: Boolean
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.waves))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        isPlaying = isPlaying
    )

    LottieAnimation(
        modifier = modifier,
        composition = composition,
        progress = { progress }
    )
}

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
    onRetry: () -> Unit = {}
) {
    var showMoreOptions by remember { mutableStateOf(false) }

    MoisesScaffold(
        title = stringResource(DesignR.string.song_details_screen_title),
        onBackClick = onBackClick,
        containerColor = Color.Transparent,
        actions = {
            if (!uiState.song?.collectionId.isNullOrEmpty()) {
                MoisesIconButton(
                    onClick = { showMoreOptions = true },
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = stringResource(DesignR.string.song_list_item_more_options_button_content_description),
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
                when {
                    uiState.isPlayerLoading -> {
                        MoisesCircularLoading(modifier = Modifier.align(Alignment.Center))
                    }

                    uiState.errorType != null -> {
                        MoisesError(
                            type = uiState.errorType,
                            onRetry = onRetry,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }

                    else -> {
                        MoisesPlayerControl(
                            modifier = Modifier.align(Alignment.BottomCenter),
                            song = uiState.song,
                            isPlaying = uiState.isPlaying,
                            onPause = onPause,
                            currentPosition = currentPosition,
                            duration = duration,
                            onSeek = onSeek,
                            onPlay = onPlay
                        )
                    }
                }
        }
    }

    if (showMoreOptions) {
        uiState.song?.let { song ->
            MoreOptionsBottomSheet(
                title = song.title,
                subtitle = song.artistName,
                imageUrl = song.artworkUrl,
                onDismissRequest = { showMoreOptions = false },
                onAlbumClick = {
                    song.collectionId?.let {
                        onPause()
                    }

                    onAlbumClick(song.collectionId.orEmpty())
                }
            )
        }
    }
}

@Composable
private fun MoisesPlayerControl(
    modifier: Modifier = Modifier,
    song: Song?,
    isPlaying: Boolean,
    onPause: () -> Unit,
    currentPosition: Long,
    duration: Long,
    onSeek: (Long) -> Unit,
    onPlay: () -> Unit
) {
    val backgroundModifier =
        Modifier.background(
            brush = Brush.verticalGradient(
                colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.8f))
            )
        )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .then(backgroundModifier)
            .navigationBarsPadding()
            .padding(MoisesSpacings.large),
        horizontalAlignment = Alignment.Start,
    ) {
        SongListItem(
            size = SongSize.LARGE,
            title = song?.title.orEmpty(),
            subtitle = song?.artistName.orEmpty(),
            imageUrl = song?.artworkUrl,
            trailingIcon = null
        )

        Spacer(Modifier.height(MoisesSpacings.medium))

        song?.previewUrl?.let {
            MoisesSlider(
                currentPosition = currentPosition,
                duration = duration,
                onSeek = onSeek
            )

            MoisesIconButton(
                imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                contentDescription = if (isPlaying) {
                    stringResource(DesignR.string.song_details_player_pause_button_content_description)
                } else {
                    stringResource(DesignR.string.song_details_player_play_button_content_description)
                },
                onClick = {
                    if (isPlaying) {
                        onPause()
                    } else {
                        onPlay()
                    }
                },
                iconSize = MoisesIconSizes.extraLarge,
                tint = MaterialTheme.colorScheme.primary,
                containerColor = White_20,
                modifier = Modifier
                    .size(72.dp)
                    .align(Alignment.CenterHorizontally)
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
                artistId = "456",
                isCollection = false
            )
        )
    )
}
