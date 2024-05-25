package com.example.eatsplorer.utilities

import retrofit2.http.GET
import retrofit2.http.Query

interface EdamamApi {

    @GET("search")
    suspend fun getRecipesByIngredient(
        @Query("q") query: String,
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("lang") language: String = "es"
    ): RespuestaAPI

    @GET("search")
    suspend fun searchRecipes(
        @Query("app_id") appId: String,
        @Query("app_key") appKey: String,
        @Query("q") query: String,
        @Query("lang") language: String = "es"
    ): RespuestaAPI
}

data class RespuestaAPI(
    val hits: List<Hit>
)

data class Hit(
    val recipe: Receta
)

data class Receta(
    val uri: String,
    val label: String,
    val image: String?,
    val yield: Int,
    val ingredientLines: List<String>?,
    val instructions: List<String>?
)

data class Ingrediente(
    val text: String
)