package com.challenge.moises.core.network.data.repositories

import com.challenge.moises.core.network.domain.models.Song
import kotlinx.coroutines.flow.Flow

interface ItunesRepository {
    fun searchSongs(query: String): Flow<List<Song>>
    fun getSongDetails(trackId: String): Flow<List<Song>>
    fun getAlbumSongs(albumId: String): Flow<List<Song>>
}
