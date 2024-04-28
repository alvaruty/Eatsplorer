package com.example.eatsplorer.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eatsplorer.DestinationScreen
import com.example.eatsplorer.R
import com.example.eatsplorer.utilities.AnalyticsManager
import com.example.eatsplorer.utilities.AuthManager
import com.example.eatsplorer.utilities.AuthRes
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(navController: NavController, analytics: AnalyticsManager, authManager: AuthManager) {
    val email = remember { mutableStateOf("") }
    val contraseña = remember { mutableStateOf("") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(32.dp)
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        ) {
            Text(
                text = "Iniciar sesión",
                style = TextStyle(
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 13.dp)
            )
            LoginForm(email, contraseña)
            Spacer(modifier = Modifier.height(16.dp))
            LoginButton(navController, email.value, contraseña.value, authManager)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿No tienes cuenta? ",
                    style = TextStyle(fontSize = 14.sp, color = Color.Black),
                )
                ClickableText(
                    text = "Crear nueva cuenta",
                    onClick = { navController.navigate(DestinationScreen.SignIn.route) },
                    modifier = Modifier.padding(bottom = 16.dp),
                )
            }

        }
        Image(
            painter = painterResource(id = R.drawable.cocinero),
            contentDescription = "Cook Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
fun LoginForm(email: MutableState<String>, contraseña: MutableState<String>) {
    Column(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        TextFieldComponent("Correo electrónico", email)
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldComponent("Contraseña", contraseña)
    }
}

@Composable
fun LoginButton(navController: NavController, email: String, contraseña: String, authManager: AuthManager) {
    val coroutineScope = rememberCoroutineScope()

    Button(
        onClick = {
            coroutineScope.launch {
                // Llama a la función suspendida para iniciar sesión
                val signInResult = authManager.signInWithEmailAndPassword(email, contraseña)
                if (signInResult is AuthRes.Success) {
                    // Si el inicio de sesión es exitoso, navega a la pantalla principal
                    navController.navigate(DestinationScreen.MainScreen.route)
                } else if (signInResult is AuthRes.Error) {
                    // Si hay un error durante el inicio de sesión, maneja el error aquí
                    // Por ejemplo, puedes mostrar un mensaje de error
                    Toast.makeText(navController.context, signInResult.errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            Color.Black // Cambia aquí el color de fondo del botón
        ),
        contentPadding = PaddingValues(vertical = 15.dp)
    ) {
        Text(
            text = "Iniciar sesión",
            style = TextStyle(
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
    }
}

@Composable
fun TextFieldComponent(
    text: String,
    textFieldValue: MutableState<String>
) {
    OutlinedTextField(
        value = textFieldValue.value,
        onValueChange = { textFieldValue.value = it },
        label = { Text(text) },
        textStyle = TextStyle(fontSize = 14.sp),
        visualTransformation = if (text == "Contraseña") PasswordVisualTransformation() else VisualTransformation.None,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun ClickableText(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Text(
        text = text,
        style = TextStyle(
            fontSize = 14.sp,
            color = Color(android.graphics.Color.parseColor("#ed6b5a")),
            fontWeight = FontWeight.Bold
        ),
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(top = 16.dp),
    )
}