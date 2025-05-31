package com.iiitdharwad.qrscanner.ui.scanner

import android.Manifest
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FlipCameraAndroid
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.iiitdharwad.qrscanner.data.ScannerMode
import com.iiitdharwad.qrscanner.ui.components.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ScannerScreen(
    viewModel: ScannerViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    LaunchedEffect(Unit) {
        if (!cameraPermissionState.status.isGranted) {
            cameraPermissionState.launchPermissionRequest()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        CenterAlignedTopAppBar(
            title = { 
                Text("QR Scanner") 
            },
            actions = {
                IconButton(onClick = { viewModel.switchMode() }) {
                    Icon(
                        imageVector = Icons.Default.FlipCameraAndroid,
                        contentDescription = "Switch Mode"
                    )
                }
            }
        )

        // Mode Indicator
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = if (uiState.scannerMode == ScannerMode.CHECK_IN) {
                    MaterialTheme.colorScheme.secondaryContainer
                } else {
                    MaterialTheme.colorScheme.tertiaryContainer
                }
            )
        ) {
            Text(
                text = "Mode: ${uiState.scannerMode.getDisplayName()}",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleMedium
            )
        }

        // Camera Preview or Permission Request
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            if (cameraPermissionState.status.isGranted) {
                CameraPreview(
                    onQRCodeDetected = { qrCode ->
                        viewModel.onQRCodeDetected(qrCode)
                    },
                    isScanning = uiState.isScanning
                )

                // Scanner overlay
                ScannerOverlay()

                // Start/Stop scanning button
                Button(
                    onClick = {
                        if (uiState.isScanning) {
                            viewModel.stopScanning()
                        } else {
                            viewModel.startScanning()
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(16.dp)
                ) {
                    Text(
                        text = if (uiState.isScanning) "Stop Scanning" else "Start Scanning"
                    )
                }
            } else {
                // Permission not granted
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Camera permission is required to scan QR codes",
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { cameraPermissionState.launchPermissionRequest() }
                    ) {
                        Text("Grant Permission")
                    }
                }
            }
        }
    }

    // Student Details Dialog
    uiState.scannedStudent?.let { student ->
        StudentDetailsDialog(
            student = student,
            scannerMode = uiState.scannerMode,
            isLoading = uiState.isLoading,
            onConfirm = { viewModel.confirmAction() },
            onCancel = { viewModel.cancelAction() }
        )
    }

    // Error/Success messages
    uiState.errorMessage?.let { message ->
        LaunchedEffect(message) {
            // Auto-dismiss after 3 seconds
            kotlinx.coroutines.delay(3000)
            viewModel.clearMessages()
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            ErrorMessage(
                message = message,
                onDismiss = { viewModel.clearMessages() }
            )
        }
    }

    uiState.successMessage?.let { message ->
        LaunchedEffect(message) {
            // Auto-dismiss after 3 seconds
            kotlinx.coroutines.delay(3000)
            viewModel.clearMessages()
        }
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            SuccessMessage(
                message = message,
                onDismiss = { viewModel.clearMessages() }
            )
        }
    }
}

@Composable
private fun CameraPreview(
    onQRCodeDetected: (String) -> Unit,
    isScanning: Boolean
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val cameraExecutor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    AndroidView(
        factory = { ctx ->
            val previewView = PreviewView(ctx)
            val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }

                val imageAnalyzer = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .build()
                    .also {
                        it.setAnalyzer(cameraExecutor) { imageProxy ->
                            if (isScanning) {
                                processImageProxy(imageProxy, onQRCodeDetected)
                            } else {
                                imageProxy.close()
                            }
                        }
                    }

                val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageAnalyzer
                    )
                } catch (exc: Exception) {
                    // Handle exception
                }
            }, ContextCompat.getMainExecutor(ctx))

            previewView
        },
        modifier = Modifier.fillMaxSize()
    )
}

@androidx.camera.core.ExperimentalGetImage
private fun processImageProxy(
    imageProxy: ImageProxy,
    onQRCodeDetected: (String) -> Unit
) {
    val mediaImage = imageProxy.image
    if (mediaImage != null) {
        val image = InputImage.fromMediaImage(mediaImage, imageProxy.imageInfo.rotationDegrees)
        val scanner = BarcodeScanning.getClient()

        scanner.process(image)
            .addOnSuccessListener { barcodes ->
                for (barcode in barcodes) {
                    when (barcode.valueType) {
                        Barcode.TYPE_TEXT, Barcode.TYPE_URL -> {
                            barcode.rawValue?.let { qrContent ->
                                onQRCodeDetected(qrContent)
                            }
                        }
                    }
                }
            }
            .addOnFailureListener {
                // Handle failure
            }
            .addOnCompleteListener {
                imageProxy.close()
            }
    } else {
        imageProxy.close()
    }
}