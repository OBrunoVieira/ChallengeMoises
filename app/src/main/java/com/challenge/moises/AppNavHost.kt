package com.challenge.moises

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.challenge.moises.common.navigation.NavRoute
import com.challenge.moises.common.navigation.serializedRoutes
import com.challenge.moises.feature.album.ui.screens.AlbumScreen
import com.challenge.moises.feature.song_details.ui.screens.SongDetailsScreen
import com.challenge.moises.feature.songs.ui.screens.SongsScreen

@Composable
fun AppNavHost() {
    val backStack = rememberNavBackStack(serializedRoutes(), NavRoute.Songs)

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
                is NavRoute.Songs -> {
                    SongsScreen(
                        onSongClick = { songId ->
                            backStack.add(NavRoute.SongDetails(songId))
                        }
                    )
                }
                is NavRoute.Album -> {
                    AlbumScreen(
                        albumId = key.id,
                        onBackClick = { 
                            if (backStack.size > 1) backStack.removeAt(backStack.size - 1) 
                        },
                        onSongClick = { songId ->
                            backStack.add(NavRoute.SongDetails(songId))
                        }
                    )
                }
                is NavRoute.SongDetails -> {
                    SongDetailsScreen(
                        songId = key.id,
                        onBackClick = { 
                            if (backStack.size > 1) backStack.removeAt(backStack.size - 1) 
                        },
                        onAlbumClick = { albumId ->
                            backStack.add(NavRoute.Album(albumId))
                        }
                    )
                }
            }
        }
    }
}
