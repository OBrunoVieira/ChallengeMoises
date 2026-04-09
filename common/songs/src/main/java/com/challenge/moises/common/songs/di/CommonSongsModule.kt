package com.challenge.moises.common.songs.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.challenge.moises.common.songs.data.repository.RecentSongsRepositoryImpl
import com.challenge.moises.common.songs.domain.repository.RecentSongsRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class CommonSongsModule {

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
