package com.hathway.projecttracker.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    primaryContainer = LightBlue,
    background = BackgroundColor,
    surface = SurfaceColor,
    onPrimary = SurfaceColor,
    onPrimaryContainer = PrimaryText,
    onBackground = PrimaryText,
    onSurface = PrimaryText,
    error = ErrorRed
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF90CAF9),
    primaryContainer = Color(0xFF0D47A1),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color(0xFF121212),
    onPrimaryContainer = Color(0xFFE3F2FD),
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0),
    error = ErrorRed
)

@Composable
fun ProTrackTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = ProTrackTypography,
        shapes = ProTrackShapes,
        content = content
    )
}
