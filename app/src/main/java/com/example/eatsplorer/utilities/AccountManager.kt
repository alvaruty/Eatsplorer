package com.example.eatsplorer.utilities

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

sealed class AuthRes {
    object Success : AuthRes()
    data class Error(val errorMessage: String) : AuthRes()
}

class AuthManager {
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

    fun signOut() {
        auth.signOut()
    }
}
