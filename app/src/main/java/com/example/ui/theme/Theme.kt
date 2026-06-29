package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Default Senior-Friendly Warm Theme
private val WarmLightColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = Color.White,
    primaryContainer = TealSecondary,
    onPrimaryContainer = Color.White,
    secondary = TealSecondary,
    onSecondary = Color.White,
    background = WarmBackground,
    onBackground = Color(0xFF1C1A17),
    surface = Color.White,
    onSurface = Color(0xFF1C1A17),
    error = MedicalRed,
    onError = Color.White,
    surfaceVariant = WarmSurface,
    onSurfaceVariant = Color(0xFF2C2A27)
)

// Dark Theme fallback
private val WarmDarkColorScheme = darkColorScheme(
    primary = TealSecondary,
    onPrimary = Color.White,
    primaryContainer = TealPrimary,
    onPrimaryContainer = Color.White,
    secondary = Color(0xFF818CF8), // Slate Indigo
    onSecondary = Color.Black,
    background = Color(0xFF0F172A), // Dark Slate Background
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E293B),    // Dark Slate Surface
    onSurface = Color(0xFFF8FAFC),
    error = MedicalRed,
    onError = Color.White,
    surfaceVariant = Color(0xFF334155), // Secondary Dark Surface
    onSurfaceVariant = Color(0xFFCBD5E1)
)

// Accessibility High-Contrast Theme (High Contrast Black with yellow/cyan highlights)
private val HighContrastColorScheme = darkColorScheme(
    primary = HighContrastPrimary,
    onPrimary = Color.Black,
    secondary = HighContrastSecondary,
    onSecondary = Color.Black,
    background = HighContrastBackground,
    onBackground = HighContrastText,
    surface = HighContrastSurface,
    onSurface = HighContrastText,
    error = HighContrastRed,
    onError = Color.Black,
    surfaceVariant = Color(0xFF222222),
    onSurfaceVariant = HighContrastText
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    highContrast: Boolean = false,
    largeText: Boolean = true,
    content: @Composable () -> Unit
) {
    val colors = when {
        highContrast -> HighContrastColorScheme
        darkTheme -> WarmDarkColorScheme
        else -> WarmLightColorScheme
    }

    MaterialTheme(
        colorScheme = colors,
        typography = if (largeText) LargeTypography else Typography,
        content = content
    )
}
