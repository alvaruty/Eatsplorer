package com.example.eatsplorer.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
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
fun RegisterScreen(navController: NavController, authManager: AuthManager, analytics: AnalyticsManager) {
    analytics.logScreenView(screenName = DestinationScreen.SignIn.route)

    val context = LocalContext.current
    val email = remember { mutableStateOf("") }
    val contraseña = remember { mutableStateOf("") }
    val scope = rememberCoroutineScope()

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
                text = "Registrarse",
                style = TextStyle(
                    fontSize = 27.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                ),
                modifier = Modifier.padding(bottom = 13.dp)
            )
            RegisterForm(email = email, contraseña = contraseña)
            Spacer(modifier = Modifier.height(16.dp))
            RegisterButton(navController, email.value, contraseña.value, authManager)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "¿Ya tienes una cuenta? ",
                    style = TextStyle(fontSize = 14.sp, color = Color.Black),
                )
                ClickableText(
                    text = "Iniciar sesión",
                    onClick = { navController.navigate(DestinationScreen.Login.route) },
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
fun RegisterForm(email: MutableState<String>, contraseña: MutableState<String>) {
    Column(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        TextFieldComponent("Correo electrónico", email) { newEmail ->
            email.value = newEmail
        }
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldComponent("Contraseña", contraseña) { newPassword ->
            contraseña.value = newPassword
        }
    }
}


@Composable
fun RegisterButton(navController: NavController, email: String, contraseña: String, authManager: AuthManager) {
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            scope.launch {
                signUp(navController, email, contraseña, authManager)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = buttonColors(
            Color.Black // Cambia aquí el color de fondo del botón
        ),
        contentPadding = PaddingValues(vertical = 15.dp)
    ) {
        Text(
            text = "Registrarse",
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
    textFieldValue: MutableState<String>,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = textFieldValue.value,
        onValueChange = {
            textFieldValue.value = it
            onValueChange(it)
        },
        label = { Text(text) },
        textStyle = TextStyle(fontSize = 14.sp),
        visualTransformation = if (text == "Contraseña") PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = if (text == "Correo electrónico") KeyboardOptions(keyboardType = KeyboardType.Email) else KeyboardOptions.Default,
        modifier = Modifier.fillMaxWidth()
    )
}

private suspend fun signUp(navController: NavController, email: String, contraseña: String, authManager: AuthManager) {
    val result = authManager.createUserWithEmailAndPassword(email, contraseña)
    if (result is AuthRes.Success) {
        Toast.makeText(navController.context, "Registro exitoso", Toast.LENGTH_SHORT).show()
        navController.navigate(DestinationScreen.Login.route)
    } else if (result is AuthRes.Error) {
        Toast.makeText(navController.context, "Error SignUp: ${result.errorMessage}", Toast.LENGTH_SHORT).show()
    }
}


