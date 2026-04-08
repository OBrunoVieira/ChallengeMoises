package com.challenge.moises.core.network

import com.challenge.moises.core.network.models.ItunesSearchResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("search")
    suspend fun search(
        @Query("term") term: String,
        @Query("entity") entity: String? = "musicTrack",
        @Query("limit") limit: Int = 20,
    ): ItunesSearchResponseDTO

    @GET("lookup")
    suspend fun songDetails(
        @Query("id") id: String,
        @Query("entity") entity: String? = "musicTrack"
    ): ItunesSearchResponseDTO

    @GET("lookup")
    suspend fun album(
        @Query("id") id: String,
        @Query("entity") entity: String? = "song"
    ): ItunesSearchResponseDTO
}
