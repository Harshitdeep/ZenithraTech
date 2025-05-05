package com.example.zenithra

import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.zenithra.UI.AppNavGraph
import com.example.zenithra.model.UserSessionManager
import com.example.zenithra.mangaScreen.MangaRepository
import com.example.zenithra.model.MangaViewModel
import com.example.zenithra.model.MangaViewModelFactory
import com.example.zenithra.model.RetrofitInstance

class MainActivity : AppCompatActivity() {
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<String>
    private var isCameraPermissionGranted = false
    private lateinit var mangaViewModel: MangaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Request Camera Permission
        requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            isCameraPermissionGranted = isGranted
            if (!isGranted) {
                Toast.makeText(this, "Camera permission is required", Toast.LENGTH_SHORT).show()
            }
        }

        checkCameraPermission()

        val repository = MangaRepository(api = RetrofitInstance.api, dao = RetrofitInstance.dao)

        val factory = MangaViewModelFactory(repository)
        mangaViewModel = ViewModelProvider(this, factory).get(MangaViewModel::class.java)

        setContent {

            setContent {
                val navController = rememberNavController()
                val context = LocalContext.current

                val startDestination = if (UserSessionManager.isLoggedIn(context)) {
                    "home"
                } else {
                    "sign_in"
                }

                AppNavGraph(
                    navController = navController,
                    requestPermissionLauncher = requestPermissionLauncher,
                    startDestination = startDestination
                )
            }
        }
    }

    private fun checkCameraPermission() {
        if (checkSelfPermission(android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            requestPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        } else {
            isCameraPermissionGranted = true
        }
    }
}
