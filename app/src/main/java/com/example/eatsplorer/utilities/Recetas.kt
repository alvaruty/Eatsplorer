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
    val image: String?,  // Nueva propiedad para la imagen
    val yield: Int,  // Nueva propiedad para las porciones
    val ingredientLines: List<String>?
)

data class Ingrediente(
    val text: String
)