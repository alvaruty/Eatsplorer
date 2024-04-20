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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eatsplorer.DestinationScreen
import com.example.eatsplorer.utilities.RecipeViewModelEdaman
import com.example.eatsplorer.R
import com.example.eatsplorer.utilities.Receta
import coil.compose.rememberAsyncImagePainter
import com.example.eatsplorer.utilities.RecipeViewModelNutritionix


@Composable
fun MyScreen(viewModel: RecipeViewModelEdaman, navController: NavController, viewModelImagen: RecipeViewModelNutritionix) {
    val isLoading = viewModel.isLoading
    val recipes = viewModel.recipes
    val error = viewModel.error

    // Llamamos a getRecommendedRecipes() al cargar la pantalla
    LaunchedEffect(Unit) {
        viewModel.getRecommendedRecipes()
    }

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
            ),
            modifier = Modifier.padding(bottom = 8.dp)
        )

        if (isLoading) {
            // Muestra un indicador de carga
            Text(text = "Cargando recetas...", modifier = Modifier.fillMaxWidth().padding(16.dp))
        } else if (error != null) {
            // Muestra un mensaje de error
            Text(text = error, modifier = Modifier.fillMaxWidth().padding(16.dp))
        } else if (recipes.isNotEmpty()) {
            // Lista de recomendados
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Filtrar las recetas para omitir la primera
                val filteredRecipes = recipes.drop(1)
                items(filteredRecipes) { recipe ->
                    RecipeItemRecomendado(recipe,viewModel,viewModelImagen)
                }
            }
        } else {
            // Mensaje si no hay recetas
            Text(text = "No se encontraron recetas recomendadas.", modifier = Modifier.fillMaxWidth().padding(16.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        // Menú inferior
        BottomMenu(navController, selectedIcon = Icons.Default.Home)
    }
}


@Composable
fun SearchBar(viewModel: RecipeViewModelEdaman) {
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
        CategoryItem("Arroz", R.drawable.arroz),
        CategoryItem("Huevo", R.drawable.huevo),
        CategoryItem("Carne", R.drawable.carne),
        CategoryItem("Pescado", R.drawable.pescado),
        CategoryItem("Pasta", R.drawable.pasta)
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
fun BottomMenu(navController: NavController, selectedIcon: ImageVector) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Home,
            contentDescription = "Inicio",
            modifier = Modifier
                .size(36.dp)
                .clickable {
                    navController.navigate(DestinationScreen.MainScreen.route) //
                },
            tint = if (selectedIcon == Icons.Default.Home) Color(android.graphics.Color.parseColor("#ed6b5a")) else Color.Black
        )

        Icon(
            Icons.Default.Favorite,
            contentDescription = "Favoritos",
            modifier = Modifier
                .size(36.dp)
                .clickable {
                    navController.navigate(DestinationScreen.Favorites.route)
                },
            tint = if (selectedIcon == Icons.Default.Favorite) Color(android.graphics.Color.parseColor("#ed6b5a")) else Color.Black
        )

        Icon(
            Icons.Default.AccountCircle,
            contentDescription = "Usuario",
            modifier = Modifier
                .size(36.dp)
                .clickable {
                    navController.navigate(DestinationScreen.Account.route)
                },
            tint = if (selectedIcon == Icons.Default.AccountCircle) Color(android.graphics.Color.parseColor("#ed6b5a")) else Color.Black
        )
    }
}

@Composable
fun RecipeItemRecomendado(recipe: Receta, viewModel: RecipeViewModelEdaman, viewModelImagen: RecipeViewModelNutritionix) {
    Card(
        modifier = Modifier
            .width(365.dp)
            .height(380.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xE7E7E7),
        )
    ) {
        Row(
            modifier = Modifier.padding(16.dp)
        ) {
            // Imagen de la receta si está disponible
            if (!recipe.image.isNullOrEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(recipe.image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = recipe.label,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(8.dp)
                )

                Text(
                    text = "Para ${recipe.yield} personas",
                    modifier = Modifier.padding(8.dp)
                )

                Text(
                    text = "Ingredientes:",
                    modifier = Modifier.padding(8.dp)
                )

                recipe.ingredientLines?.forEach { ingredient ->
                    IngredientItem(ingredient = ingredient, viewModel = viewModelImagen)
                }
            }
        }
    }
}


@Composable
fun IngredientItem(ingredient: String, viewModel: RecipeViewModelNutritionix) {
    var imageUrl by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        imageUrl = viewModel.getIngredientImage(ingredient)
    }

    Box(
        modifier = Modifier
            .padding(start = 8.dp, end = 8.dp, bottom = 8.dp)
            .size(40.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = Color.LightGray)
    ) {
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl!!),
                contentDescription = ingredient,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Text(
                text = ingredient,
                modifier = Modifier.align(Alignment.Center),
                fontSize = 12.sp
            )
        }
    }
}



