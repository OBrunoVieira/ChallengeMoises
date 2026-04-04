package com.challenge.moises.core.network.di

import com.challenge.moises.core.network.data.repositories.ItunesRepository
import com.challenge.moises.core.network.data.repositories.ItunesRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindItunesRepository(
        itunesRepositoryImpl: ItunesRepositoryImpl
    ): ItunesRepository
}
