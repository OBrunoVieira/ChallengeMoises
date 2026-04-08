package com.challenge.moises.core.network.data.repositories

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.challenge.moises.core.network.ITunesApi
import com.challenge.moises.core.network.data.paging.SongPagingSource
import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.core.network.domain.models.toSongs
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

    override fun searchSongs(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { SongPagingSource(api, query) }
        ).flow

    override fun getSongDetails(trackId: String): Flow<List<Song>> = flow {
        val songs = api.songDetails(id = trackId).results
            .toSongs()
        emit(songs)
    }.flowOn(Dispatchers.IO)

    override fun getAlbumSongs(albumId: String): Flow<List<Song>> = flow {
        val songs = api.album(id = albumId).results
            .toSongs()
        emit(songs)
    }.flowOn(Dispatchers.IO)
}
