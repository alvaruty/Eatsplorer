package com.example.eatsplorer.screens

import FirestoreManager
import android.annotation.SuppressLint
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
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
import coil.compose.rememberImagePainter
import com.example.eatsplorer.utilities.AuthManager
import com.example.eatsplorer.utilities.Recetass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect
import com.example.eatsplorer.DestinationScreen


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun FavoritesScreen(
    navController: NavController,
    firestore: FirestoreManager,
    authManager: AuthManager
) {
    var showAddRecipeDialog by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val uid = authManager.getCurrentUser()?.uid ?: ""

    var selectedTabIndex by remember { mutableStateOf(0) }
    val tabs = listOf("Mis recetas", "Otras recetas")

    val myRecipes by firestore.getNotesFlow().collectAsState(initial = emptyList())
    val publishedRecipes by firestore.getPublishedRecipesFlow().collectAsState(initial = emptyList())

    val permissionsLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allPermissionsGranted = permissions.entries.all { it.value }
        if (!allPermissionsGranted) {
            // Manejar el caso en que los permisos no fueron otorgados
        }
    }

    LaunchedEffect(Unit) {
        permissionsLauncher.launch(
            arrayOf(
                android.Manifest.permission.CAMERA,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        )
    }

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
                    onRecipeAdded = { recipe, imageUri ->
                        scope.launch {
                            firestore.addReceta(recipe, imageUri)
                        }
                        showAddRecipeDialog = false
                    },
                    onDialogDismissed = { showAddRecipeDialog = false },
                    authManager = authManager,
                )
            }
        }
    ) {
        Column {
            TabRow(
                selectedTabIndex = selectedTabIndex,
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(text = title) }
                    )
                }
            }
            when (selectedTabIndex) {
                0 -> RecipeList(navController, myRecipes, firestore, allowDelete = true) // Permitir eliminación en "Mis recetas"
                1 -> RecipeList(navController, publishedRecipes, firestore, allowDelete = false) // No permitir eliminación en "Otras recetas"
            }
            Spacer(modifier = Modifier.weight(1f))
            BottomMenu(navController, selectedIcon = Icons.Default.Favorite)
        }
    }
}

@Composable
fun RecipeList(
    navController: NavController,
    recetas: List<Recetass>,
    firestore: FirestoreManager,
    allowDelete: Boolean
) {
    if (recetas.isNotEmpty()) {
        LazyColumn {
            itemsIndexed(recetas) { index, recipe ->
                RecipeItem(
                    navController = navController,
                    recipe = recipe,
                    firestore = firestore,
                    allowDelete = allowDelete // Pasar el valor de allowDelete a RecipeItem
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
        ) {
            Spacer(modifier = Modifier.weight(1f))
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
            Icon(
                imageVector = Icons.Default.FoodBank,
                contentDescription = null,
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "No se encontraron \nRecetas",
                fontSize = 20.sp, textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.weight(1f))
            BottomMenu(navController, selectedIcon = Icons.Default.Favorite)
        }
    }
}

@Composable
fun RecipeItem(navController: NavController, recipe: Recetass, firestore: FirestoreManager, allowDelete: Boolean) {
    var showDeleteRecipeDialog by remember { mutableStateOf(false) }

    val onDeleteRecipeConfirmed: () -> Unit = {
        CoroutineScope(Dispatchers.Default).launch {
            firestore.deleteReceta(recipe.key ?: "")
        }
    }

    if (showDeleteRecipeDialog && allowDelete) { // Solo mostrar el diálogo si se permite la eliminación
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
            .fillMaxWidth()
            .clickable {
                navController.navigate(DestinationScreen.RecipeDetail.createRoute(recipe.key ?: ""))
            },
        colors = CardDefaults.cardColors(
            containerColor = Color(android.graphics.Color.parseColor("#f7f7f9")),
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (recipe.imagen.isNotEmpty()) {
                Image(
                    painter = rememberImagePainter(recipe.imagen),
                    contentDescription = null,
                    modifier = Modifier.size(100.dp)
                )
            }
            Column(modifier = Modifier.weight(3f)) {
                Text(
                    text = recipe.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recipe.ingredients,
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
fun AddRecipeDialog(
    onRecipeAdded: (Recetass, Uri?) -> Unit,
    onDialogDismissed: () -> Unit,
    authManager: AuthManager
) {
    var name by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var instructions by remember { mutableStateOf("") }
    var isPublished by remember { mutableStateOf(false) } // Nuevo estado para isPublished
    val uid = authManager.getCurrentUser()?.uid
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }

    val imageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

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
                        uid = uid.toString(),
                        isPublished = isPublished // Asignar valor a isPublished
                    )
                    onRecipeAdded(newRecipe, selectedImageUri)
                    name = ""
                    ingredients = ""
                    instructions = ""
                    isPublished = false
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
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    value = instructions,
                    onValueChange = { instructions = it },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                    label = { Text(text = "Instrucciones") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        imageLauncher.launch("image/*")
                    }
                ) {
                    Text(text = "Seleccionar Imagen")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = isPublished,
                        onCheckedChange = { isPublished = it }
                    )
                    Text(text = "Publicar receta")
                }
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
