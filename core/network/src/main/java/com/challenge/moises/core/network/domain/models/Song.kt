package com.challenge.moises.core.network.domain.models

data class Song(
    val id: String,
    val isCollection : Boolean,
    val artistId: String,
    val title: String,
    val artistName: String,
    val albumName: String?,
    val collectionId: String?,
    val artworkUrl: String?,
    val previewUrl: String?,
    val kind: String?,
    val isExplicit: Boolean = false
)
