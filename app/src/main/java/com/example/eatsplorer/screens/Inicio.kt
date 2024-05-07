package com.example.eatsplorer.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.eatsplorer.utilities.Receta
import com.example.eatsplorer.utilities.RecipeViewModelEdaman


@Composable
fun MainScreen(viewModel: RecipeViewModelEdaman) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val isLoading = viewModel.isLoading
        val recipes = viewModel.recipes
        val error = viewModel.error

        TextField(
            value = viewModel.searchQuery,
            onValueChange = { viewModel.searchQuery = it },
            placeholder = { Text("Buscar por ingrediente") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
            singleLine = true
        )

        Button(
            onClick = {  },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        ) {
            if (isLoading) {
                CircularProgressIndicator(color = Color.Blue)
            } else {
                Text("Buscar")
            }
        }

        error?.let { errorMessage ->
            Text(text = errorMessage, color = Color.Red, modifier = Modifier.padding(bottom = 8.dp))
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(recipes) { recipe ->
                RecipeItemm(recipe)
            }
        }
    }
}

@Composable
fun RecipeItemm(recipe: Receta) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = recipe.label)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Ingredientes:")
            recipe.ingredientLines?.let {
                if (it.isNotEmpty()) {
                    it.forEach { ingredient ->
                        Text(text = "- ${ingredient.toString()}")
                    }
                } else {
                    Text(text = "No se encontraron ingredientes.")
                }
            } ?: run {
                Text(text = "No se encontraron ingredientes.")
            }
        }
    }
}


