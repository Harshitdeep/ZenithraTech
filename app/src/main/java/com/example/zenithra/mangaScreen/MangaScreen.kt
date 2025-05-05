package com.example.zenithra.mangaScreen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.zenithra.App
import com.example.zenithra.model.MangaViewModel
import com.example.zenithra.model.RetrofitInstance
import com.example.zenithra.model.MangaViewModelFactory
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.layout.ContentScale
import com.example.zenithra.model.UserSessionManager
import kotlinx.coroutines.launch
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun MangaScreen(navController: NavHostController) {
    val context = LocalContext.current
    val app = context.applicationContext as App
    val dao = app.appDatabase.mangaDao()

    val api = remember { RetrofitInstance.api }
    val repository = remember { MangaRepository(api, dao) }

    val viewModel: MangaViewModel = viewModel(factory = MangaViewModelFactory(repository))
    val mangaList by viewModel.mangaList.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState() // Track the loading state

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.loadManga(page = 1)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(top = 56.dp, start = 4.dp, end = 4.dp, bottom = 4.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(mangaList) { manga ->
                    // Column for each manga item
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(2.dp)
                            .clickable {
                                val encodedTitle = URLEncoder.encode(manga.title, StandardCharsets.UTF_8.toString())
                                val encodedImageUrl = URLEncoder.encode(manga.imageUrl, StandardCharsets.UTF_8.toString())
                                navController.navigate("manga_detail/${manga.mal_id}/$encodedTitle/$encodedImageUrl")
                            }
                    ) {
                        AsyncImage(
                            model = manga.imageUrl,
                            contentDescription = manga.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .height(190.dp)
                                .fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = manga.title,
                            color = Color.White,
                            fontSize = MaterialTheme.typography.labelSmall.fontSize,
                            maxLines = 2
                        )
                    }
                }
            }
        }

        IconButton(
            onClick = {
                Toast.makeText(context, "Logout clicked", Toast.LENGTH_SHORT).show()
                coroutineScope.launch {
                    UserSessionManager.clearSession(context)
                    navController.navigate("sign_in") {
                        popUpTo(0) { inclusive = true }
                    }
                }
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Logout,
                contentDescription = "Logout",
                tint = Color.White
            )
        }
    }
}
