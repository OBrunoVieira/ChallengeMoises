package com.challenge.moises.core.network.domain.models

data class Song(
    val id: String,
    val artistId:String,
    val title: String,
    val artistName: String,
    val albumName: String?,
    val collectionId: String?,
    val artworkUrl: String?
)
