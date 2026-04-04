package com.challenge.moises.core.network

import com.challenge.moises.core.network.models.ItunesSearchResponseDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface ITunesApi {
    @GET("search")
    suspend fun search(
        @Query("term") term: String,
        @Query("entity") entity: String? = null,
        @Query("limit") limit: Int = 50
    ): ItunesSearchResponseDTO

    @GET("lookup")
    suspend fun lookup(
        @Query("id") id: String,
        @Query("entity") entity: String? = null
    ): ItunesSearchResponseDTO
}
