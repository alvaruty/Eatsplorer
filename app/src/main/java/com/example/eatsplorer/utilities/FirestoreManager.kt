package com.example.eatsplorer.utilities

import android.content.Context
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class FirestoreManager(context: Context) {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = AuthManager(context)
    private val userId = auth.getCurrentUser()?.uid

    suspend fun addReceta(receta: Recetass) {
        receta.uid = userId.toString()
        firestore.collection("recetas").add(receta).await()
    }

    suspend fun updateReceta(receta: Recetass) {
        val noteRef = receta.key?.let { firestore.collection("recetas").document(it) }
        noteRef?.set(receta)?.await()
    }

    suspend fun deleteReceta(recetaId: String) {
        val noteRef = firestore.collection("recetas").document(recetaId)
        noteRef.delete().await()
    }

    fun getNotesFlow(): Flow<List<Recetass>> = callbackFlow {
        val notesRef = firestore.collection("recetas")
            .whereEqualTo("uid", userId)
            .orderBy("name") // Cambiado de "title" a "name" según la estructura de tu documento

        val subscription = notesRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                close(error)
                return@addSnapshotListener
            }

            snapshot?.let { querySnapshot ->
                val recetas = mutableListOf<Recetass>()
                for (document in querySnapshot.documents) {
                    val receta = document.toObject(Recetass::class.java)
                    receta?.key = document.id
                    receta?.let { recetas.add(it) }
                }
                trySend(recetas).isSuccess
            }
        }
        awaitClose { subscription.remove() }
    }

}
