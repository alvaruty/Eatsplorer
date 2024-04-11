package com.example.eatsplorer.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.eatsplorer.utilities.RecipeViewModel
import com.example.eatsplorer.R


@Composable
fun MyScreen(viewModel: RecipeViewModel) {
    val isLoading = viewModel.isLoading
    val recipes = viewModel.recipes
    val error = viewModel.error
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra de búsqueda
        SearchBar(viewModel)

        Spacer(modifier = Modifier.height(16.dp))

        // Título de categorías
        Text(
            text = "Categorías",
            color = Color.Black,
            style = TextStyle(
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Filtros de categorías
        Categories()

        Spacer(modifier = Modifier.height(16.dp))

        // Título de recomendados
        Text(
            text = "Recomendados",
            color = Color.Black,
            style = TextStyle(
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de recomendados
        RecommendedList()

        Spacer(modifier = Modifier.weight(1f))

        // Menú inferior
        BottomMenu()
    }
}

@Composable
fun SearchBar(viewModel: RecipeViewModel) {
    OutlinedTextField(
        value = viewModel.searchQuery,
        onValueChange = { viewModel.searchQuery = it },
        placeholder = { Text("Buscar") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        shape = RoundedCornerShape(percent = 50) // Redondear completamente
    )
}

@Composable
fun Categories() {
    val categories = listOf(
        CategoryItem("Pollo", R.drawable.pollo),
        CategoryItem("Arroz", R.drawable.pollo),
        CategoryItem("Huevo", R.drawable.pollo),
        CategoryItem("Carne", R.drawable.pollo),
        CategoryItem("Pescado", R.drawable.pollo)
    )

    LazyRow(modifier = Modifier.fillMaxWidth()) {
        items(categories) { category ->
            CategoryItem(category)
        }
    }
}

@Composable
fun CategoryItem(category: CategoryItem) {
    var isSelected by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .padding(8.dp)
            .width(80.dp)  // Ancho
            .height(100.dp)
            .background(
                if (isSelected) Color.Black else Color.White,
                shape = RoundedCornerShape(16.dp) // Redondear esquinas
            )
            .border(
                width = 1.dp,
                color = Color.Gray,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable {
                isSelected = !isSelected
                // Aquí puedes realizar la acción que desees al hacer clic
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = category.imageRes),
                contentDescription = category.name,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = category.name,
                color = if (isSelected) Color.White else Color.Black
            )
        }
    }
}

data class CategoryItem(val name: String, val imageRes: Int)



@Composable
fun RecommendedList() {
    // Aquí iría la implementación de tu lista de elementos recomendados
    // Puedes usar LazyColumn o LazyRow según tu diseño
}

@Composable
fun BottomMenu() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Home, contentDescription = "Inicio", modifier = Modifier.size(36.dp))
        Icon(Icons.Default.Favorite, contentDescription = "Favoritos", modifier = Modifier.size(36.dp))
        Icon(Icons.Default.AccountCircle, contentDescription = "Usuario", modifier = Modifier.size(36.dp))
    }
}


