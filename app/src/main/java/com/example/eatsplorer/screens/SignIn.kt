package com.example.eatsplorer.screens

import androidx.compose.foundation.Image
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eatsplorer.DestinationScreen
import com.example.eatsplorer.R

@Composable
fun RegisterScreen(navController: NavController) {
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
            RegisterForm()
            Spacer(modifier = Modifier.height(16.dp))
            RegisterButton(navController)
            //Spacer(modifier = Modifier.height(16.dp))
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
fun RegisterForm() {
    Column(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        TextFieldComponent(text = "Nombre")
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldComponent(text = "Correo electrónico")
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldComponent(text = "Contraseña")
    }
}

@Composable
fun RegisterButton(navController: NavController) {
    Button(
        onClick = { navController.navigate(DestinationScreen.Login.route) },
        modifier = Modifier.fillMaxWidth(),
        colors = buttonColors(
            Color.Black // Cambia aquí el color de fondo del botón
        ),
        contentPadding = PaddingValues(vertical = 16.dp), // Ajusta el espacio vertical aquí
        content = {
            Text(
                text = "Registrarse",
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
    )
}
