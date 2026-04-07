package com.challenge.moises.core.network.data.repositories

import com.challenge.moises.core.network.ITunesApi
import com.challenge.moises.core.network.domain.models.Song
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItunesRepositoryImpl @Inject constructor(
    private val api: ITunesApi
) : ItunesRepository {

    override fun searchSongs(query: String): Flow<List<Song>> = flow {
        val songs = api.search(term = query, entity = "musicTrack").results
            .map { it.toDomain() }
            .sortedByDescending { it.kind == "music-video" }
        emit(songs)
    }.flowOn(Dispatchers.IO)

    override fun getAlbumSongs(albumId: String): Flow<List<Song>> = flow {
        val songs = api.lookup(id = albumId, entity = "musicTrack").results
            .map { it.toDomain() }
        emit(songs)
    }.flowOn(Dispatchers.IO)
}
