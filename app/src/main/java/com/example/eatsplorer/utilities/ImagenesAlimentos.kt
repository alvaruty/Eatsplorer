package com.example.eatsplorer.utilities

import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NutritionixApi {

    @GET("search/instant")
    suspend fun searchFood(
        @Header("x-app-id") appId: String,
        @Header("x-app-key") appKey: String,
        @Query("query") query: String
    ): NutritionixResponse
}
data class NutritionixResponse(
    val foods: List<Food>
)

data class Food(
    val food_name: String,
    val photo: Photo?
)

data class Photo(
    val thumb: String?,
    val highres: String?
)
