package com.example.eatsplorer.utilities

import android.content.Context
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RealtimeManager(context: Context) {
    private val databaseReference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("recetas")
    private val authManager = AuthManager(context)

    fun addReceta(receta: Recetass) {
        val key = databaseReference.push().key
        if (key != null) {
            databaseReference.child(key).setValue(receta)
        }
    }

    fun deleteReceta(RecetatId: String) {
        databaseReference.child(RecetatId).removeValue()
    }

    fun updateContact(contactId: String, updatedReceta: Recetass) {
        databaseReference.child(contactId).setValue(updatedReceta)
    }

    fun getRecetasFlow(): Flow<List<Recetass>> {
        val idFilter = authManager.getCurrentUser()?.uid
        val flow = callbackFlow {
            val listener = databaseReference.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val contacts = snapshot.children.mapNotNull { snapshot ->
                        val contact = snapshot.getValue(Recetass::class.java)
                        snapshot.key?.let { contact?.copy(key = it) }
                    }
                    trySend(contacts.filter { it.uid == idFilter }).isSuccess
                }

                override fun onCancelled(error: DatabaseError) {
                    close(error.toException())
                }
            })
            awaitClose { databaseReference.removeEventListener(listener) }
        }
        return flow
    }
}