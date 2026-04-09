package com.challenge.moises.feature.songs.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.challenge.moises.feature.songs.data.repository.RecentSongsRepository
import com.challenge.moises.feature.songs.data.repository.RecentSongsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class SongsModule {

    @Binds
    @Singleton
    abstract fun bindRecentSongsRepository(
        impl: RecentSongsRepositoryImpl
    ): RecentSongsRepository

    companion object {
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "recent_songs_prefs")

        @Provides
        @Singleton
        fun provideRecentSongsDataStore(
            @ApplicationContext context: Context
        ): DataStore<Preferences> = context.dataStore
    }
}
