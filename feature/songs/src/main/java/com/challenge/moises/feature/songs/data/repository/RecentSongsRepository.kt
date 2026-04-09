package com.challenge.moises.feature.songs.data.repository

import com.challenge.moises.core.network.domain.models.Song
import kotlinx.coroutines.flow.Flow

interface RecentSongsRepository {
    fun getRecentSongs(): Flow<List<Song>>
    suspend fun saveRecentSong(song: Song)
    suspend fun removeRecentSong(song: Song)
}