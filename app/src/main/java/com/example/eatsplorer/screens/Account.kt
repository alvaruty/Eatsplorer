package com.example.eatsplorer.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eatsplorer.utilities.AuthManager


@Composable
fun AccountScreen(
    navController: NavController,
    authManager: AuthManager,
    onSignOut: () -> Unit,
) {
    var changePasswordDialogVisible by remember { mutableStateOf(false) }
    var deleteAccountDialogVisible by remember { mutableStateOf(false) }
    val userEmail = authManager.getUserEmail() // Obtener el correo electrónico del usuario
    var passwordResetSuccess by remember { mutableStateOf<Boolean?>(null) } // Estado para mostrar el resultado del restablecimiento de contraseña
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier.align(Alignment.Center).padding(top = 30.dp)
            ) {
                Text(
                    text = "Mi Cuenta",
                    style = TextStyle(
                        fontSize = 27.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    ),
                    modifier = Modifier.padding(bottom = 16.dp, start = 40.dp)
                )
                Spacer(modifier = Modifier.height(25.dp))
                Icon(
                    Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .size(220.dp)
                        .padding(16.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Correo electrónico del usuario
                Text(
                    text = userEmail, // Mostrar el correo electrónico obtenido
                    style = TextStyle(
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para cambiar contraseña
        Button(
            onClick = { changePasswordDialogVisible = true }, // Mostrar el diálogo de cambio de contraseña
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                Color.Transparent
            ),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text(
                text = "Cambiar Contraseña",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para cerrar sesión
        Button(
            onClick = {
                authManager.signOut()
                onSignOut()
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                Color.Transparent
            ),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text(
                text = "Cerrar Sesión",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón para eliminar cuenta
        Button(
            onClick = { deleteAccountDialogVisible = true }, // Mostrar el diálogo de confirmación de eliminación de cuenta
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                Color.Transparent
            ),
            contentPadding = PaddingValues(vertical = 16.dp)
        ) {
            Text(
                text = "Eliminar Cuenta",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Red
                )
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        // Menú inferior
        BottomMenu(navController, selectedIcon = Icons.Default.AccountCircle)
    }

    // Mostrar el resultado del restablecimiento de contraseña
    passwordResetSuccess?.let { success ->
        val message = if (success) {
            "Se ha enviado un correo para restablecer la contraseña."
        } else {
            "Error al enviar el correo para restablecer la contraseña."
        }
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

    // Cuadro de diálogo para cambiar contraseña
    if (changePasswordDialogVisible) {
        ChangePasswordDialog(
            onClose = { changePasswordDialogVisible = false },
            onChangePassword = { context, email ->
                authManager.sendPasswordResetEmail(email) { success ->
                    passwordResetSuccess = success
                    changePasswordDialogVisible = false
                }
            },
            currentPassword = userEmail // Pasar el correo electrónico como contraseña actual
        )
    }

    // Cuadro de diálogo para eliminar cuenta
    if (deleteAccountDialogVisible) {
        DeleteAccountDialog(
            onClose = { deleteAccountDialogVisible = false },
            onDeleteAccount = {
                authManager.deleteAccount { success ->
                    if (success) {
                        Toast.makeText(context, "Cuenta eliminada correctamente.", Toast.LENGTH_LONG).show()
                        onSignOut()
                    } else {
                        Toast.makeText(context, "Error al eliminar la cuenta.", Toast.LENGTH_LONG).show()
                    }
                    deleteAccountDialogVisible = false
                }
            }
        )
    }
}

@Composable
fun ChangePasswordDialog(
    onClose: () -> Unit,
    onChangePassword: (Context, String) -> Unit, // Contexto y nueva contraseña como parámetros
    currentPassword: String // Agregar la contraseña actual como un parámetro
) {
    val context = LocalContext.current

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(text = "Cambiar Contraseña") },
        text = {
            Column {
                Text("¿Deseas enviar un correo para cambiar la contraseña a $currentPassword?")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onChangePassword(context, currentPassword)
                    onClose()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(
                onClick = { onClose() }
            ) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun DeleteAccountDialog(
    onClose: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(text = "Eliminar Cuenta") },
        text = {
            Column {
                Text("¿Estás seguro de que deseas eliminar tu cuenta? Esta acción no se puede deshacer.")
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onDeleteAccount()
                    onClose()
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(
                onClick = { onClose() }
            ) {
                Text("Cancelar")
            }
        }
    )
}