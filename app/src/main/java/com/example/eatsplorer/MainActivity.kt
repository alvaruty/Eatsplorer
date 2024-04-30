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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.eatsplorer.screens.AccountScreen
import com.example.eatsplorer.screens.LoginScreen
import com.example.eatsplorer.screens.FavoritesScreen // Importa la nueva pantalla de favoritos
import com.example.eatsplorer.screens.MyScreen
import com.example.eatsplorer.screens.RegisterScreen
import com.example.eatsplorer.ui.theme.EatsplorerTheme
import com.example.eatsplorer.utilities.AnalyticsManager
import com.example.eatsplorer.utilities.AuthManager
import com.example.eatsplorer.utilities.RecipeViewModelEdaman
import com.example.eatsplorer.utilities.RecipeViewModelFirebase
import android.content.Context

sealed class DestinationScreen(var route: String){
    object Login : DestinationScreen("Login")
    object SignIn : DestinationScreen("Signin")
    object MainScreen : DestinationScreen("Main")
    object Favorites : DestinationScreen("Favorites")
    object Account : DestinationScreen("Account")
    object ChangePassword : DestinationScreen("ChangePassword")
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
                    AppNavigation(this)
                    //val recipeViewModel = remember { RecipeViewModel() } //borrar

                    //MainScreen(recipeViewModel)
                }
            }
        }
    }

    @Composable
    fun AppNavigation(context: Context) {
        val navController = rememberNavController()
        val recipeViewModel = remember { RecipeViewModelEdaman() }
        val recipeViewModelImagenes = remember { RecipeViewModelFirebase() }
        val analytics: AnalyticsManager = AnalyticsManager(context)
        val authManager: AuthManager = AuthManager()

        val onSignOut: () -> Unit = {
            // Aquí puedes definir la lógica para cerrar sesión
            // Por ejemplo, navegar a la pantalla de inicio de sesión
            navController.navigate(DestinationScreen.Login.route)
        }


        NavHost(navController = navController, startDestination = DestinationScreen.Login.route) {
            composable(DestinationScreen.Login.route) {
                LoginScreen(navController, analytics, authManager)
            }
            composable(DestinationScreen.SignIn.route) {
                RegisterScreen(navController, authManager, analytics)
            }
            composable(DestinationScreen.MainScreen.route) {
                MyScreen(viewModel = recipeViewModel, navController, recipeViewModelImagenes)
            }
            composable(DestinationScreen.Favorites.route) {
                FavoritesScreen(navController)
            }
            composable(DestinationScreen.Account.route) {
                AccountScreen(navController, authManager, onSignOut, analytics)
            }
        }
    }
}

