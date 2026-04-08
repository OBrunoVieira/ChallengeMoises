package com.challenge.moises.core.network.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.challenge.moises.core.network.ITunesApi
import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.core.network.domain.models.toSongs

class SongPagingSource(
    private val api: ITunesApi,
    private val query: String
) : PagingSource<Int, Song>() {

    override fun getRefreshKey(state: PagingState<Int, Song>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Song> {
        val position = params.key ?: 0
        return try {
            val response = api.search(
                term = query,
                limit = params.loadSize,
            )
            val songs = response.results.toSongs()

            LoadResult.Page(
                data = songs,
                prevKey = if (position == 0) null else position - params.loadSize,
                nextKey = if (songs.isEmpty()) null else position + songs.size
            )
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}
