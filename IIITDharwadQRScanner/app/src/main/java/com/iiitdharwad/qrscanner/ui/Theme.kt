package com.iiitdharwad.qrscanner.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightThemeColors = lightColorScheme(
    primary = Color(0xFF0066CC), // IIIT Blue
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1E4FF),
    onPrimaryContainer = Color(0xFF001D36),
    secondary = Color(0xFF4CAF50), // Check-in Green
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFB8F5B9),
    onSecondaryContainer = Color(0xFF002201),
    tertiary = Color(0xFFFF9800), // Check-out Orange
    onTertiary = Color.Black,
    tertiaryContainer = Color(0xFFFFDDB5),
    onTertiaryContainer = Color(0xFF2B1D00)
)

private val DarkThemeColors = darkColorScheme(
    primary = Color(0xFF93C5FF),
    onPrimary = Color(0xFF003258),
    primaryContainer = Color(0xFF004880),
    onPrimaryContainer = Color(0xFFD1E4FF),
    secondary = Color(0xFF9DDC9F),
    onSecondary = Color(0xFF003910),
    secondaryContainer = Color(0xFF005317),
    onSecondaryContainer = Color(0xFFB8F5B9),
    tertiary = Color(0xFFFFB95C),
    onTertiary = Color(0xFF472A00),
    tertiaryContainer = Color(0xFF673D00),
    onTertiaryContainer = Color(0xFFFFDDB5)
)

@Composable
fun IIITDharwadQRScannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkThemeColors else LightThemeColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}