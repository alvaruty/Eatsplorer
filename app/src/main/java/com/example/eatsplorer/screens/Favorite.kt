package com.example.eatsplorer.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FoodBank
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.eatsplorer.utilities.AuthManager
import com.example.eatsplorer.utilities.FirestoreManager
import com.example.eatsplorer.utilities.Recetass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavoritesScreen(navController: NavController, firestore: FirestoreManager, authManager: AuthManager) {
    var showAddRecipeDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val recetas by firestore.getNotesFlow().collectAsState(emptyList())

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                containerColor = Color(android.graphics.Color.parseColor("#ed6b5a")),
                onClick = {
                    showAddRecipeDialog = true
                },
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add Recipe")
            }

            if (showAddRecipeDialog) {
                AddRecipeDialog(
                    onRecipeAdded = { recipe ->
                        scope.launch {
                            firestore.addReceta(recipe)
                        }
                        showAddRecipeDialog = false
                    },
                    onDialogDismissed = { showAddRecipeDialog = false },
                    authManager = authManager,
                )
            }
        }
    ) {
        if(!recetas.isNullOrEmpty()) {
            LazyColumn {
                itemsIndexed(recetas) { index, recipe ->
                    RecipeItem(recipe = recipe, firestore = firestore)
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                Spacer(modifier = Modifier.weight(1f))
                // Menú inferior
                BottomMenu(navController, selectedIcon = Icons.Default.Favorite)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(50.dp))
                Icon(imageVector = Icons.Default.FoodBank, contentDescription = null, modifier = Modifier.size(150.dp))
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "No se encontraron \nRecetas",
                    fontSize = 20.sp, textAlign = TextAlign.Center)
                Spacer(modifier = Modifier.weight(1f))
                // Menú inferior
                BottomMenu(navController, selectedIcon = Icons.Default.Favorite)
            }
        }
    }


}

@Composable
fun RecipeItem(recipe: Recetass, firestore: FirestoreManager) {
    var showDeleteRecipeDialog by remember { mutableStateOf(false) }

    val onDeleteRecipeConfirmed: () -> Unit = {
        CoroutineScope(Dispatchers.Default).launch {
            firestore.deleteReceta(recipe.key ?: "")
        }
    }

    if (showDeleteRecipeDialog) {
        DeleteRecipeDialog(
            onConfirmDelete = {
                onDeleteRecipeConfirmed()
                showDeleteRecipeDialog = false
            },
            onDismiss = {
                showDeleteRecipeDialog = false
            }
        )
    }

    Card(
        modifier = Modifier
            .padding(start = 15.dp, end = 15.dp, top = 15.dp, bottom = 0.dp)
            .fillMaxWidth())
    {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(modifier = Modifier.weight(3f)) {
                Text(
                    text = recipe.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recipe.ingredients,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recipe.instructions,
                    fontWeight = FontWeight.Thin,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis)
            }
            Row(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.Center,
            ) {
                IconButton(
                    onClick = {
                        showDeleteRecipeDialog = true
                    },
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete Icon")
                }
            }
        }
    }
}

@Composable
fun AddRecipeDialog(onRecipeAdded: (Recetass) -> Unit, onDialogDismissed: () -> Unit, authManager: AuthManager) {
    var name by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var uid = authManager.getCurrentUser()?.uid

    AlertDialog(
        onDismissRequest = {},
        title = { Text(text = "Agregar Receta") },
        confirmButton = {
            Button(
                onClick = {
                    val newRecipe = Recetass(
                        name = name,
                        ingredients = ingredients,
                        instructions = instructions,
                        uid = uid.toString()
                    )
                    onRecipeAdded(newRecipe)
                    name = ""
                    ingredients = ""
                    instructions = ""
                }
            ) {
                Text(text = "Agregar")
            }
        },
        dismissButton = {
            Button(
                onClick = {
                    onDialogDismissed()
                }
            ) {
                Text(text = "Cancelar")
            }
        },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    label = { Text(text = "Nombre") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = ingredients,
                    onValueChange = { ingredients = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    label = { Text(text = "Ingredientes") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = instructions,
                    onValueChange = { instructions = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    label = { Text(text = "Instrucciones") }
                )
            }
        }
    )
}

@Composable
fun DeleteRecipeDialog(onConfirmDelete: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Eliminar receta") },
        text = { Text("¿Estás seguro que deseas eliminar la receta?") },
        confirmButton = {
            Button(
                onClick = onConfirmDelete
            ) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            Button(
                onClick = onDismiss
            ) {
                Text("Cancelar")
            }
        }
    )
}

