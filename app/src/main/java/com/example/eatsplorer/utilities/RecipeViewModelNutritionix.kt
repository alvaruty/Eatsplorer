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

class RecipeViewModelNutritionix : ViewModel() {
    var searchQuery by mutableStateOf("")
    var recipes by mutableStateOf<List<Receta>>(emptyList())
    var isLoading by mutableStateOf(false)
    var error by mutableStateOf<String?>(null)

    private val nutritionixRetrofit = Retrofit.Builder()
        .baseUrl("https://trackapi.nutritionix.com/v2/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val nutritionixApi = nutritionixRetrofit.create(NutritionixApi::class.java)
    private val nutritionixAppId = "f50bfeaf" // Reemplaza con tu APP ID de Nutritionix
    private val nutritionixAppKey = "6383f1171b3c51f1398972928b85dfb2" // Reemplaza con tu APP KEY de Nutritionix

    fun getIngredientImage(ingredient: String): String? {
        var imageUrl: String? = null
        viewModelScope.launch {
            try {
                val response = nutritionixApi.searchFood(nutritionixAppId, nutritionixAppKey, ingredient)
                if (response.foods.isNotEmpty()) {
                    imageUrl = response.foods[0].photo?.highres ?: response.foods[0].photo?.thumb
                }
            } catch (e: Exception) {
                Log.e("API_ERROR", e.message ?: "Unknown error") // Imprimir el error
            }
        }
        return imageUrl
    }
}