package com.example.zenithra.UI

import FaceRecognitionScreen
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.zenithra.mangaScreen.MangaRepository
import com.example.zenithra.mangaScreen.MangaScreen
import com.example.zenithra.model.MangaViewModel
import com.example.zenithra.model.MangaViewModelFactory
import com.example.zenithra.model.RetrofitInstance

@Composable
fun HomeScreen(navController: NavHostController, requestPermissionLauncher: ActivityResultLauncher<String>) {
    val items = listOf(Screen.Manga, Screen.FaceRecognition)
    var selectedScreen by remember { mutableStateOf<Screen>(Screen.Manga) }

    val repository = remember {
        MangaRepository(RetrofitInstance.api, RetrofitInstance.dao)
    }

    val viewModel: MangaViewModel = viewModel(
        factory = MangaViewModelFactory(repository)
    )


    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        label = { Text(screen.title) },
                        selected = selectedScreen == screen,
                        onClick = {
                            selectedScreen = screen
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (selectedScreen) {
                Screen.Manga -> MangaScreen(navController)
                Screen.FaceRecognition -> FaceRecognitionScreen(requestPermissionLauncher = requestPermissionLauncher)
            }
        }
    }
}



sealed class Screen(val route: String, val title: String, val icon: ImageVector) {
    object Manga : Screen("manga", "Manga", Icons.AutoMirrored.Filled.MenuBook)
    object FaceRecognition : Screen("face", "Face", Icons.Default.Face)
}
