package com.challenge.moises.feature.songs.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.core.preferences.BasePreferencesManager
import com.challenge.moises.feature.songs.domain.repository.RecentSongsRepository
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import javax.inject.Inject

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "recent_songs_prefs")

class RecentSongsRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context
) : BasePreferencesManager(context.dataStore, Gson()), RecentSongsRepository {

    companion object {
        private val RECENT_SONGS_KEY = stringPreferencesKey("recent_songs")
    }

    override fun getRecentSongs(): Flow<List<Song>> {
        val type = object : TypeToken<List<Song>>() {}.type
        return readObject(RECENT_SONGS_KEY, type, emptyList())
    }

    override suspend fun saveRecentSong(song: Song) {
        val type = object : TypeToken<List<Song>>() {}.type
        val currentList = readObject<List<Song>>(RECENT_SONGS_KEY, type, emptyList())
            .firstOrNull()
            ?.toMutableList() ?: mutableListOf()

        currentList.removeAll { it.id == song.id }
        currentList.add(0, song)

        if (currentList.size > 20) {
            currentList.removeAt(currentList.size - 1)
        }

        writeObject(RECENT_SONGS_KEY, currentList)
    }
}
