package com.example.zenithra.mangaScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.zenithra.model.RetrofitInstance.dao
import com.example.zenithra.model.RetrofitInstance
import android.content.Context
import androidx.compose.ui.platform.LocalContext
import com.example.zenithra.model.UserSessionManager

@Composable
fun MangaDetailScreen(
    navController: NavHostController,
    id: Int,
    title: String,
    imageUrl: String
) {
    val context = LocalContext.current // Get the context here

    val api = remember { RetrofitInstance.api }
    val repository = remember { MangaRepository(api, dao) }

    val mangaDetailState = remember { mutableStateOf<MangaDetail?>(null) }
    val errorState = remember { mutableStateOf<String?>(null) }

    // Load the like state from SharedPreferences
    var isLiked by remember { mutableStateOf(UserSessionManager.getLikeState(context, id)) }

    LaunchedEffect(id) {
        try {
            val mangaDetails = repository.getMangaDetails(id)
            mangaDetailState.value = mangaDetails
        } catch (e: Exception) {
            errorState.value = "Failed to load manga details."
        }
    }

    val mangaDetail = mangaDetailState.value

    when {
        errorState.value != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = errorState.value ?: "Unknown error",
                    color = Color.White,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        mangaDetail == null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())  // Enables scrolling
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Left: Image
                        AsyncImage(
                            model = mangaDetail.imageUrl,
                            contentDescription = mangaDetail.title,
                            modifier = Modifier
                                .fillMaxHeight()
                                .weight(1f)
                                .padding(top = 50.dp)
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        // Right: Title
                        Text(
                            text = mangaDetail.title,
                            color = Color.White,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Show the synopsis
                    Text(
                        text = mangaDetail.synopsis,
                        color = Color.White,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                // Star icon for "Like" functionality, placed in the top-right corner
                IconButton(
                    onClick = {
                        isLiked = !isLiked
                        UserSessionManager.saveLikeState(context, id, isLiked)
                    },
                    modifier = Modifier
                        .align(Alignment.TopEnd)  // Ensures it aligns to the top-right corner
                        .padding(16.dp)
                ) {
                    Icon(
                        imageVector = if (isLiked) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = if (isLiked) "Unlike" else "Like",
                        tint = Color.Yellow
                    )
                }
            }
        }
    }
}
