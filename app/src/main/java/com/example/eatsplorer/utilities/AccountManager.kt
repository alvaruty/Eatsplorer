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

    suspend fun updatePassword(email: String, currentPassword: String, newPassword: String): AuthRes {
        return try {
            // Reautenticar al usuario antes de cambiar la contraseña
            val currentUser = auth.currentUser
            val credential = EmailAuthProvider.getCredential(email, currentPassword)
            currentUser?.reauthenticate(credential)?.await()

            // Cambiar la contraseña
            currentUser?.updatePassword(newPassword)?.await()

            AuthRes.Success
        } catch (e: Exception) {
            AuthRes.Error(e.message ?: "Unknown error")
        }
    }

    suspend fun onChangePassword(
        authManager: AuthManager,
        email: String,
        currentPassword: String,
        newPassword: String,
        context: Context, // Añade el contexto como parámetro
        onClose: () -> Unit // Añade la función onClose como parámetro
    ) {
        val result = authManager.updatePassword(email, currentPassword, newPassword)
        if (result is AuthRes.Success) {
            Toast.makeText(context, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show()
            onClose()
        } else if (result is AuthRes.Error) {
            Toast.makeText(context, "Error al cambiar la contraseña: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
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
}
