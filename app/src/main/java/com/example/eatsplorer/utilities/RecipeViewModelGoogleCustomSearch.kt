package com.example.eatsplorer.utilities

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipeViewModelGoogleCustomSearch : ViewModel() {

    private val customSearchApi = Retrofit.Builder()
        .baseUrl("https://www.googleapis.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GoogleCustomSearchApi::class.java)

    private val apiKey = "AIzaSyCKfiMGsAL9Wn98EhDAR_zk-C_OBywfkhc" // Reemplaza con tu API KEY de Google
    private val cx = "c18c1d9068b024620" // Reemplaza con tu ID de búsqueda personalizado

    suspend fun getIngredientImage(ingredient: String): String? {
        return withContext(Dispatchers.IO) {
            try {
                val response = customSearchApi.searchImage(ingredient, apiKey, cx, "png")
                if (response.items.isNotEmpty()) {
                    response.items[0].link
                } else {
                    null
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}

