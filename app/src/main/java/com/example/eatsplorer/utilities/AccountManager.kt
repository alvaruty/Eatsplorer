package com.example.eatsplorer.utilities

import android.content.Context
import android.widget.Toast
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.tasks.await

sealed class AuthRes {
    object Success : AuthRes()
    data class Error(val errorMessage: String) : AuthRes()
}

class AuthManager(private val context: Context) {
    private val auth = FirebaseAuth.getInstance()

    suspend fun createUserWithEmailAndPassword(email: String, password: String): AuthRes {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            AuthRes.Success
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun signInWithEmailAndPassword(email: String, password: String): AuthRes {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            AuthRes.Success
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Unknown error")
        }
    }

    fun getCurrentUser(): FirebaseUser? {
        return FirebaseAuth.getInstance().currentUser
    }

    fun signOut() {
        auth.signOut()
    }

    // Método para obtener el correo electrónico del usuario actual
    fun getUserEmail(): String {
        val currentUser = auth.currentUser
        return currentUser?.email ?: ""
    }

    fun sendPasswordResetEmail(email: String, callback: (Boolean) -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }

    fun deleteAccount(callback: (Boolean) -> Unit) {
        val user = auth.currentUser
        user?.delete()
            ?.addOnCompleteListener { task ->
                callback(task.isSuccessful)
            }
    }
}
