package com.example.eatsplorer.screens

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
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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


@Composable
fun LoginScreen(navController: NavController) {
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
            LoginForm()
            Spacer(modifier = Modifier.height(16.dp))
            LoginButton(navController)
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
                    onClick = { /* Handle navigation to sign up screen */ },
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
                .height(320.dp)
                .align(Alignment.BottomCenter)
        )
    }
}


@Composable
fun LoginForm() {
    Column(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        TextFieldComponent(text = "Correo electrónico")
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldComponent(text = "Contraseña")
    }
}

@Composable
fun LoginButton(navController: NavController) {
    Button(
        onClick = { navController.navigate(DestinationScreen.MainScreen.route) },
        modifier = Modifier.fillMaxWidth(),
        colors = buttonColors(
            Color.Black // Cambia aquí el color de fondo del botón
        ),
        contentPadding = PaddingValues(vertical = 16.dp), // Ajusta el espacio vertical aquí
        content = {
            Text(
                text = "Iniciar sesión",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    )
}

@Composable
fun TextFieldComponent(text: String) {
    var textFieldValue by remember { mutableStateOf("") }
    OutlinedTextField(
        value = textFieldValue,
        onValueChange = { textFieldValue = it },
        label = {
            Text(
                text = text,
                style = TextStyle(
                    fontSize = 14.sp,
                )
            )
        },
        textStyle = TextStyle(fontSize = 14.sp, fontWeight = FontWeight.Bold),
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