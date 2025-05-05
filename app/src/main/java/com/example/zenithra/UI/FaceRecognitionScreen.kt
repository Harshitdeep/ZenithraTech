import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Rect
import android.os.Build
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.OptIn
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.*
import java.util.concurrent.Executors
import androidx.core.content.ContextCompat
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.google.mlkit.vision.face.FaceDetectorOptions
import com.google.mlkit.vision.face.FaceDetection

@SuppressLint("UnsafeOptInUsageError")
@Composable
fun FaceRecognitionScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    requestPermissionLauncher: ActivityResultLauncher<String> // Pass the launcher here
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val previewView = remember {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }
    val faces = remember { mutableStateListOf<Face>() }

    val cameraExecutor = remember { Executors.newSingleThreadExecutor() }

    // Check for Camera Permission
    var permissionGranted by remember { mutableStateOf(false) }

    // Check for permission and request if necessary
    LaunchedEffect(true) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when {
                ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                    permissionGranted = true
                }
                else -> {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA) // Use the launcher here
                }
            }
        }
    }

    if (!permissionGranted) {
        // Show a message explaining why the app needs the camera
        Toast.makeText(context, "Camera permission is required", Toast.LENGTH_SHORT).show()
        return
    }

    LaunchedEffect(true) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().apply {
                setSurfaceProvider(previewView.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor) { imageProxy ->
                        processImageProxy(imageProxy, faces)
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    cameraSelector,
                    preview,
                    imageAnalyzer
                )
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(context, "Camera binding failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }, ContextCompat.getMainExecutor(context)) // Ensure this runs on main thread
    }

    Box(modifier = modifier) {
        AndroidView(factory = { previewView }, modifier = Modifier.fillMaxSize())

        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasWidth = size.width
            val canvasHeight = size.height

            val rectWidth = canvasWidth * 0.5f
            val rectHeight = canvasHeight * 0.4f
            val topLeft = Offset(
                (canvasWidth - rectWidth) / 2f,
                (canvasHeight - rectHeight) / 2f
            )

            val referenceRect = android.graphics.RectF(
                topLeft.x,
                topLeft.y,
                topLeft.x + rectWidth,
                topLeft.y + rectHeight
            )

            val isAnyFaceInside = faces.any { face ->
                val bounds = face.boundingBox
                val faceCenterX = bounds.centerX() * (canvasWidth / 480f) // scale X
                val faceCenterY = bounds.centerY() * (canvasHeight / 640f) // scale Y
                referenceRect.contains(faceCenterX, faceCenterY)
            }

            val rectColor = if (isAnyFaceInside) Color.Green else Color.Red

            drawRect(
                color = rectColor,
                topLeft = topLeft,
                size = Size(rectWidth, rectHeight),
                style = Stroke(width = 5f)
            )
        }
    }
}

//// Function to check if face is within the reference rectangle (center of the screen)
//private fun isFaceWithinReferenceRectangle(bounds: Rect): Boolean {
//    val centerX = bounds.centerX()
//    val centerY = bounds.centerY()
//
//    // Define the center of the screen and the size of the reference rectangle
//    val screenCenterX = 500 // You can use screen metrics dynamically
//    val screenCenterY = 800
//    val referenceWidth = 400
//    val referenceHeight = 400
//
//    // Check if the face is within the reference rectangle
//    return (centerX in (screenCenterX - referenceWidth / 2)..(screenCenterX + referenceWidth / 2) &&
//            centerY in (screenCenterY - referenceHeight / 2)..(screenCenterY + referenceHeight / 2))
//}

@OptIn(ExperimentalGetImage::class)
private fun processImageProxy(imageProxy: ImageProxy, faces: SnapshotStateList<Face>) {
    val mediaImage = imageProxy.image ?: run {
        imageProxy.close()
        return
    }

    val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)

    val options = FaceDetectorOptions.Builder()
        .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
        .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_NONE)
        .build()

    val detector = FaceDetection.getClient(options)

    detector.process(image)
        .addOnSuccessListener { result ->
            faces.clear()
            faces.addAll(result)
        }
        .addOnFailureListener { it.printStackTrace() }
        .addOnCompleteListener {
            imageProxy.close()
        }
}
