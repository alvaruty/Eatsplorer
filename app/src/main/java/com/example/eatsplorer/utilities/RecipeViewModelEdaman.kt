package com.example.eatsplorer.utilities

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RecipeViewModelEdaman : ViewModel() {

    private val firebaseRepository = FirebaseRepository() // Instancia de FirebaseRepository

    var searchQuery by mutableStateOf("")
    var recipes by mutableStateOf<List<Receta>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://api.edamam.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val edamamApi = retrofit.create(EdamamApi::class.java)
    private val appId = "748bce42" // Reemplaza con tu APP ID
    private val appKey = "a74d860d83404b548c8c30e068cad9a8" // Reemplaza con tu APP KEY

    fun searchRecipes() {
        isLoading = true
        viewModelScope.launch {
            try {
                val response = edamamApi.getRecipesByIngredient(searchQuery, appId, appKey, "es")
                if (response.hits.isNotEmpty()) {
                    recipes = response.hits.map { it.recipe }
                    error = null
                } else {
                    error = "No se encontraron recetas"
                }
            } catch (e: Exception) {
                error = "Error al buscar recetas: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun getRecommendedRecipes() {
        isLoading = true
        viewModelScope.launch {
            try {
                val response = edamamApi.searchRecipes(appId, appKey, "recommended", "es") // Utilizamos un término "recommended" ficticio para obtener recetas recomendadas
                Log.d("API_RESPONSE", response.toString()) // Imprimir la respuesta de la API
                if (response.hits.isNotEmpty()) {
                    recipes = response.hits.map { it.recipe }
                    error = null
                } else {
                    error = "No se encontraron recetas recomendadas"
                }
            } catch (e: Exception) {
                error = "Error al buscar recetas recomendadas: ${e.message}"
                Log.e("API_ERROR", e.message ?: "Unknown error") // Imprimir el error
            } finally {
                isLoading = false
            }
        }
    }

    suspend fun getIngredientImage(ingredient: String): String? {
        return firebaseRepository.getIngredientImageUrl(ingredient)
    }

}