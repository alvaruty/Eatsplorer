package com.example.eatsplorer.screens

import android.accounts.AccountManager
import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eatsplorer.utilities.AnalyticsManager
import com.example.eatsplorer.utilities.AuthManager
import androidx.compose.ui.platform.LocalContext


@Composable
fun AccountScreen(
    navController: NavController,
    authManager: AuthManager,
    onSignOut: () -> Unit,
    analytics: AnalyticsManager,
) {
    var changePasswordDialogVisible by remember { mutableStateOf(false) }
    val userEmail = authManager.getUserEmail() // Obtener el correo electrónico del usuario

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Box para alinear el icono y el correo electrónico en el centro
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            contentAlignment = Alignment.Center // Alineación central vertical y horizontal
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
                // Imagen de perfil (sustituir con la imagen real del usuario)
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
                Color.White
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
                Color.White
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

        Spacer(modifier = Modifier.weight(1f)) // Espacio flexible para empujar el menú hacia abajo

        // Menú inferior
        BottomMenu(navController, selectedIcon = Icons.Default.AccountCircle)
    }

    // Cuadro de diálogo para cambiar contraseña
    if (changePasswordDialogVisible) {
        ChangePasswordDialog(
            onClose = { changePasswordDialogVisible = false },
            onChangePassword = { context, currentPassword ->
                // No es necesario llamar a onChangePassword aquí
            },
            currentPassword = authManager.getUserEmail() // Pasar el correo electrónico como contraseña actual
        )
    }
}



@Composable
fun ChangePasswordDialog(
    onClose: () -> Unit,
    onChangePassword: (Context, String) -> Unit, // Contexto y nueva contraseña como parámetros
    currentPassword: String // Agregar la contraseña actual como un parámetro
) {
    var newPassword by remember { mutableStateOf("") }

    val context = LocalContext.current

    // Lógica para confirmar el cambio de contraseña
    fun handleConfirm() {
        onChangePassword(context, newPassword)
        onClose() // Cerrar el diálogo después de confirmar
    }

    // Lógica para cancelar el cambio de contraseña
    fun handleCancel() {
        onClose()
    }

    AlertDialog(
        onDismissRequest = onClose,
        title = { Text(text = "Cambiar Contraseña") },
        text = {
            Column {
                // Campo de texto para ingresar la nueva contraseña
                OutlinedTextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    label = { Text("Nueva Contraseña") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { handleConfirm() }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            Button(
                onClick = { handleCancel() }
            ) {
                Text("Cancelar")
            }
        }
    )
}
