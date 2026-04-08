package com.challenge.moises.core.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItunesTrackDTO(
    @SerializedName("wrapperType")
    val wrapperType: String?,

    @SerializedName("kind")
    val kind: String?,

    @SerializedName("artistId")
    val artistId: Long?,

    @SerializedName("collectionId")
    val collectionId: Long?,

    @SerializedName("trackId")
    val trackId: Long?,

    @SerializedName("artistName")
    val artistName: String?,

    @SerializedName("collectionName")
    val collectionName: String?,

    @SerializedName("trackName")
    val trackName: String?,

    @SerializedName("previewUrl")
    val previewUrl: String?,

    @SerializedName("artworkUrl100")
    val artworkUrl100: String?,

    @SerializedName("trackExplicitness")
    val trackExplicitness: String?
) : Parcelable
