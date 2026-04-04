package com.challenge.moises.core.network.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class ItunesSearchResponseDTO(
    @SerializedName("resultCount")
    val resultCount: Int,
    
    @SerializedName("results")
    val results: List<ItunesTrackDTO>
) : Parcelable
