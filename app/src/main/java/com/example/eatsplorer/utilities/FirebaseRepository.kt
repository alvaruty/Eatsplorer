package com.example.eatsplorer.utilities

import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

class FirebaseRepository {

    private val storage = Firebase.storage
    private val storageReference = storage.reference.child("ingredient_images")

    suspend fun getIngredientImageUrl(ingredientName: String): String? {
        return try {
            val imageRef = storageReference.child("$ingredientName.png") // Asumiendo que las imágenes están en formato PNG
            val url = imageRef.downloadUrl.await()
            url.toString()
        } catch (e: Exception) {
            null
        }
    }
}
