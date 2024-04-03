package com.example.eatsplorer.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalBar
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun MainScreen() {
    Scaffold(
        topBar = { /* Aquí puedes agregar una barra de navegación superior si es necesario */ },
        content = {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                TopCategoriesSection()
                Spacer(modifier = Modifier.height(16.dp))
                RecommendedSection()
            }
        }
    )
}

@Composable
fun TopCategoriesSection() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Top Categories",
            style = TextStyle(
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Aquí puedes colocar los iconos de las categorías
            //CategoryIcon(icon = Icons.Default.Noodles, label = "Noodle")
            CategoryIcon(icon = Icons.Default.LocalMall, label = "Meat")
            CategoryIcon(icon = Icons.Default.Spa, label = "Salad")
            CategoryIcon(icon = Icons.Default.LocalBar, label = "Drink")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { /* Manejar acción de "View more" */ },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(text = "View more")
        }
    }
}

@Composable
fun RecommendedSection() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "Recommended",
            style = TextStyle(
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )
        // Aquí puedes colocar el elemento de recomendación (por ejemplo, la tarjeta del plato de ramen)
        RecommendedItem()
    }
}

@Composable
fun CategoryIcon(icon: ImageVector, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Text(
            text = label,
            style = TextStyle(fontSize = 14.sp, color = Color.Black),
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}

@Composable
fun RecommendedItem() {
    // Aquí puedes colocar la tarjeta del elemento recomendado (por ejemplo, el plato de ramen)
    // Puedes usar Card, Image, Text, etc. para construir la apariencia deseada
}
