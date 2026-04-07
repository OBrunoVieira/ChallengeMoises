package com.challenge.moises.common.navigation

import androidx.navigation3.runtime.NavKey
import androidx.savedstate.serialization.SavedStateConfiguration
import kotlinx.serialization.Serializable
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Serializable
sealed interface NavRoute: NavKey {
    @Serializable
    data object Songs : NavRoute
    
    @Serializable
    data class Album(val id: String) : NavRoute
    
    @Serializable
    data class SongDetails(val id: String) : NavRoute
}

fun serializedRoutes() = SavedStateConfiguration {
    serializersModule = SerializersModule {
        polymorphic(NavKey::class) {
            subclass(NavRoute.Songs::class, NavRoute.Songs.serializer())
            subclass(NavRoute.Album::class, NavRoute.Album.serializer())
            subclass(NavRoute.SongDetails::class, NavRoute.SongDetails.serializer())
        }
    }
}