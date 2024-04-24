package com.example.eatsplorer.utilities

import androidx.lifecycle.ViewModel

class RecipeViewModelFirebase : ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    suspend fun getIngredientImage(ingredient: String): String? {
        return firebaseRepository.getIngredientImageUrl(ingredient)
    }
}
