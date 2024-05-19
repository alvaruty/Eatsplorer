package com.example.eatsplorer.utilities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class RecipeViewModelFirebase : ViewModel() {

    private val firebaseRepository = FirebaseRepository()

    // LiveData para observar la lista de recetas
    private val _recipes = MutableLiveData<List<Recetass>>()
    val recipes: LiveData<List<Recetass>>
        get() = _recipes


    suspend fun getIngredientImage(ingredient: String): String? {
        return firebaseRepository.getIngredientImageUrl(ingredient)
    }


}
