package com.challenge.moises

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.challenge.moises.common.navigation.NavKey
import com.challenge.moises.feature.album.ui.screens.AlbumScreen
import com.challenge.moises.feature.song_details.ui.screens.SongDetailsScreen
import com.challenge.moises.feature.songs.ui.screens.SongsScreen

@Composable
fun AppNavHost() {
    val backStack = remember { mutableStateListOf<NavKey>(NavKey.Songs) }

    BackHandler(enabled = backStack.size > 1) {
        if (backStack.size > 1) {
            backStack.removeAt(backStack.size - 1)
        }
    }

    NavDisplay(
        backStack = backStack,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        onBack = { 
            if (backStack.size > 1) backStack.removeAt(backStack.size - 1) 
        }
    ) { key ->
        NavEntry(key = key) {
            when (key) {
                is NavKey.Songs -> {
                    SongsScreen(
                        onSongClick = { songId ->
                            backStack.add(NavKey.SongDetails(songId))
                        }
                    )
                }
                is NavKey.Album -> {
                    AlbumScreen(
                        albumId = key.id,
                        onBackClick = { 
                            if (backStack.size > 1) backStack.removeAt(backStack.size - 1) 
                        },
                        onSongClick = { songId ->
                            backStack.add(NavKey.SongDetails(songId))
                        }
                    )
                }
                is NavKey.SongDetails -> {
                    SongDetailsScreen(
                        songId = key.id,
                        onBackClick = { 
                            if (backStack.size > 1) backStack.removeAt(backStack.size - 1) 
                        },
                        onAlbumClick = { albumId ->
                            backStack.add(NavKey.Album(albumId))
                        }
                    )
                }
            }
        }
    }
}
