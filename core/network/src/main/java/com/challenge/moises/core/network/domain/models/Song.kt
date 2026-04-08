package com.challenge.moises.core.network.domain.models

import com.challenge.moises.core.network.models.ItunesTrackDTO

data class Song(
    val id: String,
    val isCollection: Boolean,
    val artistId: String,
    val title: String,
    val artistName: String,
    val albumName: String?,
    val collectionId: String?,
    val artworkUrl: String?,
    val previewUrl: String?,
    val hasVideo: Boolean = false,
    val isExplicit: Boolean = false
)

fun List<ItunesTrackDTO>.toSongs() = map { it.toSong() }


fun ItunesTrackDTO.toSong() = Song(
    id = trackId?.toString().orEmpty(),
    isCollection = wrapperType == "collection",
    artistId = artistId?.toString().orEmpty(),
    title = trackName.orEmpty(),
    artistName = artistName.orEmpty(),
    albumName = collectionName,
    collectionId = collectionId?.toString(),
    artworkUrl = artworkUrl100,
    previewUrl = previewUrl,
    hasVideo = kind == "music-video",
    isExplicit = trackExplicitness == "explicit"
)
