package com.challenge.moises.common.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface NavKey {
    @Serializable
    data object Songs : NavKey
    
    @Serializable
    data class Album(val id: String) : NavKey
    
    @Serializable
    data class SongDetails(val id: String) : NavKey
}
