package com.example.eatsplorer.screens

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
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
import coil.compose.rememberAsyncImagePainter
import com.example.eatsplorer.DestinationScreen
import com.example.eatsplorer.R
import com.example.eatsplorer.utilities.Receta
import com.example.eatsplorer.utilities.RecipeViewModelEdaman
import com.example.eatsplorer.utilities.RecipeViewModelFirebase


@Composable
fun MyScreen(viewModel: RecipeViewModelEdaman, navController: NavController, viewModelFirebase: RecipeViewModelFirebase) {
    var showRecommended by remember { mutableStateOf(true) } // Estado para controlar la visibilidad de la sección "Recomendados"
    var selectedCategories by remember { mutableStateOf(emptyList<String>()) } // Estado para las categorías seleccionadas

    val isLoading = viewModel.isLoading
    val recipes = viewModel.recipes
    val error = viewModel.error

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Barra de búsqueda
        SearchBar(viewModel) {
            showRecommended = it.isEmpty() // Actualizar el estado basado en si el campo de búsqueda está vacío
        }

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
        Categories { categories ->
            selectedCategories = categories
            viewModel.getRecipesByCategory(categories)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Título de recomendados (mostrar solo si el campo de búsqueda está vacío)
        if (showRecommended) {
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
            // Ejecutar getRecommendedRecipes solo una vez al cargar la pantalla
            LaunchedEffect(showRecommended) {
                viewModel.getRecommendedRecipes()
            }
        }


        if (isLoading) {
            // Muestra un indicador de carga
            Text(text = "Cargando recetas...", modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp))
        } else if (error != null) {
            // Muestra un mensaje de error
            Text(text = error, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp))
        } else if (recipes.isNotEmpty()) {
            // Lista de recomendados
            LazyRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Filtrar las recetas para omitir la primera
                val filteredRecipes = recipes.drop(1)
                items(filteredRecipes) { recipe ->
                    RecipeItemRecomendado(recipe, viewModel, viewModelFirebase)
                }
            }
        } else {
            // Mensaje si no hay recetas
            Text(text = "No se encontraron recetas recomendadas.", modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp))
        }

        Spacer(modifier = Modifier.weight(1f))

        // Menú inferior
        BottomMenu(navController, selectedIcon = Icons.Default.Home)
    }
}


@Composable
fun SearchBar(viewModel: RecipeViewModelEdaman, onSearch: (String) -> Unit) {
    OutlinedTextField(
        value = viewModel.searchQuery,
        onValueChange = { newValue ->
            viewModel.searchQuery = newValue
            // Llamar a la función onSearch cuando el valor cambie
            onSearch(newValue)
        },
        placeholder = { Text("Buscar") },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 5.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        shape = RoundedCornerShape(percent = 50), // Redondear completamente
        keyboardActions = KeyboardActions(
            onSearch = {
                // Realizar búsqueda cuando se presione Enter
                viewModel.searchRecipes()
            }
        )
    )
}

@Composable
fun Categories(onCategoriesSelected: (List<String>) -> Unit) {
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
            CategoryItem(category.name, category.imageRes) { categoryName, isSelected ->
                if (isSelected) {
                    onCategoriesSelected(listOf(categoryName))
                } else {
                    onCategoriesSelected(emptyList())
                }
            }
        }
    }
}

@Composable
fun CategoryItem(name: String, @DrawableRes imageRes: Int, onCategorySelected: (String, Boolean) -> Unit) {
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
                onCategorySelected(name, isSelected)
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = name,
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
fun RecipeItemRecomendado(
    recipe: Receta,
    viewModel: RecipeViewModelEdaman,
    viewModelFirebase: RecipeViewModelFirebase
) {
    Card(
        modifier = Modifier
            .width(365.dp)
            .height(380.dp)
            .padding(20.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(android.graphics.Color.parseColor("#EAEAEA")),
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
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
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                Text(
                    text = "Ingredientes:",
                    modifier = Modifier.padding(8.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
            ) {
                // Mostrar solo los primeros 5 ingredientes
                val limitedIngredients = recipe.ingredientLines?.take(5) ?: emptyList()
                LazyRow(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(limitedIngredients) { ingredient ->
                        var imageUrl by remember { mutableStateOf<String?>(null) }

                        LaunchedEffect(Unit) {
                            imageUrl = viewModelFirebase.getIngredientImage(ingredient)
                            print(imageUrl)
                        }

                        IngredientItem(ingredient = ingredient, imageUrl = imageUrl)
                    }
                }
            }
        }
    }
}

@Composable
fun IngredientItem(ingredient: String, imageUrl: String?) {
    Box(
        modifier = Modifier
            .width(50.dp)
            .height(50.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(color = Color.White)
    ) {
        if (imageUrl != null) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
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


