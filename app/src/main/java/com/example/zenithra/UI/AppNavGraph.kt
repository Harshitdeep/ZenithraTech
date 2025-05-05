package com.example.zenithra.UI

import androidx.activity.result.ActivityResultLauncher
import androidx.navigation.compose.NavHost
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.zenithra.mangaScreen.MangaDetailScreen
import com.example.zenithra.mangaScreen.MangaScreen

@Composable
fun AppNavGraph(
    navController: NavHostController,
    requestPermissionLauncher: ActivityResultLauncher<String>,
    startDestination: String // 👈 Add this

) {
    NavHost(navController = navController, startDestination = startDestination) {
        composable("sign_in") {
            Sign_In_Screen(
                onSignUpClick = {
                    navController.navigate("sign_up")
                },
                onSignInSuccess = {
                    navController.navigate("home") {
                        popUpTo("sign_in") { inclusive = true }
                    }
                }
            )
        }

        composable("sign_up") {
            SignUpScreen(
                onSignUpSuccess = {
                    navController.navigate("sign_in") {
                        popUpTo("sign_up") { inclusive = true }
                    }
                },
                onSignInClick = {
                    navController.popBackStack() // or navigate("sign_in")
                }
            )
        }

        composable("home") {
            HomeScreen(navController , requestPermissionLauncher = requestPermissionLauncher) // replace with your real Home screen
        }

        composable("manga_screen") {
            MangaScreen(navController)
        }
        composable(
            route = "manga_detail/{id}/{title}/{imageUrl}",
            arguments = listOf(
                navArgument("id") { type = NavType.IntType },
                navArgument("title") { type = NavType.StringType },
                navArgument("imageUrl") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: -1
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val imageUrl = backStackEntry.arguments?.getString("imageUrl") ?: ""
            MangaDetailScreen(navController, id, title, imageUrl)
        }

    }
}
