package com.challenge.moises.feature.songs.di

import com.challenge.moises.feature.songs.data.repository.RecentSongsRepositoryImpl
import com.challenge.moises.feature.songs.domain.repository.RecentSongsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
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
}
