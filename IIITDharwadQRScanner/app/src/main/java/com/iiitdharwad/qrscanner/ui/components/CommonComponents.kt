package com.iiitdharwad.qrscanner.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(
    message: String,
    onDismiss: () -> Unit
) {
    Snackbar(
        modifier = Modifier.padding(16.dp),
        action = {
            TextButton(onClick = onDismiss) {
                Text("DISMISS")
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = "Error"
            )
        }
    ) {
        Text(text = message)
    }
}

@Composable
fun SuccessMessage(
    message: String,
    onDismiss: () -> Unit
) {
    Snackbar(
        modifier = Modifier.padding(16.dp),
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
        action = {
            TextButton(onClick = onDismiss) {
                Text("DISMISS")
            }
        },
        icon = {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success"
            )
        }
    ) {
        Text(text = message)
    }
}

@Composable
fun ScannerOverlay(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0x80000000))
    ) {
        // Scanner frame in the center
        Box(
            modifier = Modifier
                .size(250.dp)
                .align(Alignment.Center)
                .background(Color.Transparent),
            contentAlignment = Alignment.Center
        ) {
            // Border
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(4.dp)
            ) {
                // Top-left corner
                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .height(4.dp)
                        .background(Color.White)
                )
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(30.dp)
                        .background(Color.White)
                )

                // Top-right corner
                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .height(4.dp)
                        .align(Alignment.TopEnd)
                        .background(Color.White)
                )
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(30.dp)
                        .align(Alignment.TopEnd)
                        .background(Color.White)
                )

                // Bottom-left corner
                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .height(4.dp)
                        .align(Alignment.BottomStart)
                        .background(Color.White)
                )
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(30.dp)
                        .align(Alignment.BottomStart)
                        .background(Color.White)
                )

                // Bottom-right corner
                Box(
                    modifier = Modifier
                        .width(30.dp)
                        .height(4.dp)
                        .align(Alignment.BottomEnd)
                        .background(Color.White)
                )
                Box(
                    modifier = Modifier
                        .width(4.dp)
                        .height(30.dp)
                        .align(Alignment.BottomEnd)
                        .background(Color.White)
                )
            }
        }

        // Scanning instruction text at the bottom
        Text(
            text = "Place the QR code inside the frame",
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 80.dp),
            color = Color.White,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ActionButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.onPrimary,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        ),
        enabled = enabled
    ) {
        Text(text = text)
    }
}