package com.example.eatsplorer

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.eatsplorer.screens.LoginScreen
import com.example.eatsplorer.screens.MainScreen
import com.example.eatsplorer.ui.theme.EatsplorerTheme
import com.example.eatsplorer.utilities.RecipeViewModel

sealed class DestinationScreen(var route: String){
    object Login : DestinationScreen("Login")
    object Inicio : DestinationScreen("Main")
}
class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            EatsplorerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation()

                }

            }

        }
    }
    @Composable
    fun AppNavigation(){
        val navController = rememberNavController()
        val recipeViewModel = remember { RecipeViewModel() }
        NavHost(navController = navController, startDestination = DestinationScreen.Login.route){
            composable(DestinationScreen.Login.route){
                LoginScreen(navController)
            }
            composable(DestinationScreen.Inicio.route){
                MainScreen(viewModel = recipeViewModel)
            }
        }
    }
}

