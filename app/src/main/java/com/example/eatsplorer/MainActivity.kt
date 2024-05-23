package com.example.eatsplorer

import FirestoreManager
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.eatsplorer.screens.AccountScreen
import com.example.eatsplorer.screens.FavoritesScreen
import com.example.eatsplorer.screens.LoginScreen
import com.example.eatsplorer.screens.MyScreen
import com.example.eatsplorer.screens.RecipeDetailScreen
import com.example.eatsplorer.screens.RegisterScreen
import com.example.eatsplorer.ui.theme.EatsplorerTheme
import com.example.eatsplorer.utilities.AnalyticsManager
import com.example.eatsplorer.utilities.AuthManager
import com.example.eatsplorer.utilities.RealtimeManager
import com.example.eatsplorer.utilities.Recetass
import com.example.eatsplorer.utilities.RecipeViewModelEdaman
import com.example.eatsplorer.utilities.RecipeViewModelFirebase

sealed class DestinationScreen(var route: String){
    object Login : DestinationScreen("Login")
    object SignIn : DestinationScreen("Signin")
    object MainScreen : DestinationScreen("Main")
    object Favorites : DestinationScreen("Favorites")
    object Account : DestinationScreen("Account")
    object RecipeDetail : DestinationScreen("recipeDetail/{recipeKey}") {
        fun createRoute(recipeKey: String) = "recipeDetail/$recipeKey"
    }
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
        val recipeViewModelFirebase = remember { RecipeViewModelFirebase() }
        val analytics: AnalyticsManager = AnalyticsManager(context)
        val authManager: AuthManager = remember { AuthManager(context) }
        val realtimeManager: RealtimeManager = remember { RealtimeManager(context) }
        val firestore: FirestoreManager = remember { FirestoreManager(context) }

        val onSignOut: () -> Unit = {
            // Cerrar sesión del AuthManager
            authManager.signOut()
            // Navegar a la pantalla de inicio de sesión
            navController.navigate(DestinationScreen.Login.route) {
                popUpTo(navController.graph.startDestinationId) {
                    inclusive = true
                }
            }
        }

        NavHost(navController = navController, startDestination = DestinationScreen.Login.route) {
            composable(DestinationScreen.Login.route) {
                LoginScreen(navController, analytics, authManager)
            }
            composable(DestinationScreen.SignIn.route) {
                RegisterScreen(navController, authManager, analytics)
            }
            composable(DestinationScreen.MainScreen.route) {
                MyScreen(viewModel = recipeViewModel, navController, recipeViewModelFirebase)
            }
            composable(DestinationScreen.Favorites.route) {
                FavoritesScreen(navController, firestore, authManager)
            }
            composable(DestinationScreen.Account.route) {
                AccountScreen(navController, authManager, onSignOut, analytics)
            }
            composable(
                route = DestinationScreen.RecipeDetail.route,
                arguments = listOf(navArgument("recipeKey") { type = NavType.StringType })
            ) { backStackEntry ->
                val recipeKey = backStackEntry.arguments?.getString("recipeKey")
                var recipe by remember { mutableStateOf<Recetass?>(null) }

                LaunchedEffect(recipeKey) {
                    recipe = recipeKey?.let { firestore.getRecetaByKey(it) }
                }

                recipe?.let {
                    RecipeDetailScreen(navController = navController, recipe = it)
                }
            }

        }
    }
}

