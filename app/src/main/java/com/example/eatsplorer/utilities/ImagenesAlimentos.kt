package com.example.eatsplorer.utilities

import retrofit2.http.GET
import retrofit2.http.Query

interface GoogleCustomSearchApi {

    @GET("customsearch/v1")
    suspend fun searchImage(
        @Query("q") query: String,
        @Query("key") apiKey: String,
        @Query("cx") cx: String,
        @Query("searchType") searchType: String = "image"
    ): GoogleCustomSearchResponse
}

data class GoogleCustomSearchResponse(
    val items: List<GoogleCustomSearchItem>
)

data class GoogleCustomSearchItem(
    val link: String
)
