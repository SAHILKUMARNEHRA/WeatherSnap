package com.trackzio.weathersnap.ui.screens.camera

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.trackzio.weathersnap.ui.components.OutlinePillButton
import com.trackzio.weathersnap.ui.components.PrimaryPillButton
import com.trackzio.weathersnap.ui.screens.draft.DraftPhoto
import com.trackzio.weathersnap.ui.screens.draft.ReportDraftViewModel
import com.trackzio.weathersnap.ui.util.findActivity
import com.trackzio.weathersnap.ui.util.compressJpegToFile
import java.io.File
import java.util.UUID

@Composable
fun CameraScreen(
    onClose: () -> Unit,
) {
    val context = LocalContext.current
    val activity = LocalContext.current.findActivity()
    val draftViewModel: ReportDraftViewModel = hiltViewModel(checkNotNull(activity))

    var hasPermission by remember { mutableStateOf(hasCameraPermission(context)) }
    val requestPermission = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasPermission = granted }
    )

    LaunchedEffect(Unit) {
        if (!hasPermission) requestPermission.launch(Manifest.permission.CAMERA)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (hasPermission) {
            CameraPreview(
                modifier = Modifier.fillMaxSize(),
                onCapture = { originalFile ->
                    val photosDir = File(context.filesDir, "photos").apply { mkdirs() }
                    val compressedFile = File(photosDir, "compressed_${UUID.randomUUID()}.jpg")
                    val result = compressJpegToFile(
                        originalFile = originalFile,
                        outputFile = compressedFile,
                        maxDimensionPx = 1280,
                        quality = 75
                    )
                    draftViewModel.setPhoto(
                        DraftPhoto(
                            originalPath = originalFile.absolutePath,
                            compressedPath = result.compressedFile.absolutePath,
                            originalSizeBytes = result.originalSizeBytes,
                            compressedSizeBytes = result.compressedSizeBytes,
                        )
                    )
                    onClose()
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Camera permission is required to capture a photo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
                Spacer(modifier = Modifier.height(14.dp))
                PrimaryPillButton(
                    text = "Grant Permission",
                    onClick = { requestPermission.launch(Manifest.permission.CAMERA) }
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp)
                .padding(top = 18.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                androidx.compose.material3.Text(
                    text = "Custom Camera",
                    style = androidx.compose.material3.MaterialTheme.typography.headlineSmall,
                    color = androidx.compose.material3.MaterialTheme.colorScheme.onSurface
                )
                OutlinePillButton(text = "Close", onClick = onClose)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(horizontal = 18.dp)
                .padding(bottom = 18.dp),
        ) {
            PrimaryPillButton(
                text = "Capture",
                onClick = { CameraCaptureBus.requestCapture() },
                modifier = Modifier.fillMaxWidth(),
                enabled = hasPermission
            )
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun CameraPreview(
    modifier: Modifier,
    onCapture: (File) -> Unit,
) {
    val context = LocalContext.current
    val lifecycleOwner = checkNotNull(LocalContext.current.findActivity())

    var imageCapture by remember { mutableStateOf<ImageCapture?>(null) }
    var cameraError by remember { mutableStateOf<String?>(null) }
    val previewView = remember(context) {
        PreviewView(context).apply {
            scaleType = PreviewView.ScaleType.FILL_CENTER
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
        }
    }

    DisposableEffect(lifecycleOwner, previewView) {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
        val executor = ContextCompat.getMainExecutor(context)

        cameraProviderFuture.addListener(
            {
                try {
                    val cameraProvider = cameraProviderFuture.get()
                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }
                    val capture = ImageCapture.Builder()
                        .setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY)
                        .build()

                    imageCapture = capture

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        CameraSelector.DEFAULT_BACK_CAMERA,
                        preview,
                        capture
                    )
                    cameraError = null
                } catch (t: Throwable) {
                    imageCapture = null
                    cameraError = t.message ?: "Failed to start camera"
                }
            },
            executor
        )

        onDispose {
            try {
                if (cameraProviderFuture.isDone) {
                    cameraProviderFuture.get().unbindAll()
                }
            } catch (_: Throwable) {
            }
        }
    }

    Box(modifier = modifier) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { previewView }
        )

        if (cameraError != null) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = cameraError ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.9f)
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        CameraCaptureBus.captureRequests.collect {
            val capture = imageCapture ?: return@collect
            val photosDir = File(context.filesDir, "photos").apply { mkdirs() }
            val originalFile = File(photosDir, "original_${UUID.randomUUID()}.jpg")
            val output = ImageCapture.OutputFileOptions.Builder(originalFile).build()
            capture.takePicture(
                output,
                ContextCompat.getMainExecutor(context),
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                        onCapture(originalFile)
                    }

                    override fun onError(exception: ImageCaptureException) {
                    }
                }
            )
        }
    }
}

private fun hasCameraPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
}
