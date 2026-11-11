package com.hathway.projecttracker.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import org.jetbrains.compose.resources.painterResource
import projecttracker.shared.generated.resources.Res
import projecttracker.shared.generated.resources.home_bg
import projecttracker.shared.generated.resources.home_bg_dark

// ==========================================
// THEME STATE
// ==========================================

val LocalDarkTheme = compositionLocalOf { false }

// ==========================================
// BACKGROUND IMAGE
// ==========================================

@Composable
fun rememberBackgroundPainter(): Painter {
    return if (LocalDarkTheme.current) {
        painterResource(Res.drawable.home_bg_dark)
    } else {
        painterResource(Res.drawable.home_bg)
    }
}

// ==========================================
// LIGHT COLOR SCHEME
// ==========================================

private val LightColorScheme = lightColorScheme(
    primary = PrimaryPurple,
    onPrimary = Color.White,

    primaryContainer = LightPurple,
    onPrimaryContainer = PrimaryText,

    secondary = PrimaryBlue,
    onSecondary = Color.White,

    secondaryContainer = LightBlue,
    onSecondaryContainer = PrimaryText,

    tertiary = AccentPurple,
    onTertiary = Color.White,

    background = BackgroundColor,
    onBackground = PrimaryText,

    surface = SurfaceColor,
    onSurface = PrimaryText,

    surfaceVariant = SurfaceVariantColor,
    onSurfaceVariant = SecondaryText,

    outline = BorderColor,
    outlineVariant = DividerColor,

    error = ErrorRed,
    onError = Color.White,

    scrim = Color(0x80000000)
)

// ==========================================
// DARK COLOR SCHEME
// ==========================================

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF9B7BFF),
    onPrimary = Color(0xFF180B3A),

    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkPrimaryText,

    secondary = Color(0xFF82AEFF),
    onSecondary = Color(0xFF071B3D),

    secondaryContainer = DarkSurfaceVariant,
    onSecondaryContainer = DarkPrimaryText,

    tertiary = Color(0xFFC0A0FF),
    onTertiary = Color(0xFF241044),

    background = DarkBackgroundColor,
    onBackground = DarkPrimaryText,

    surface = DarkSurfaceColor,
    onSurface = DarkPrimaryText,

    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkSecondaryText,

    outline = DarkBorderColor,
    outlineVariant = DarkDividerColor,

    error = DarkErrorRed,
    onError = Color(0xFF3B0808),

    scrim = Color(0x99000000)
)

// ==========================================
// MAIN APPLICATION THEME
// ==========================================

@Composable
fun ProTrackTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    CompositionLocalProvider(
        LocalDarkTheme provides darkTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = ProTrackTypography,
            shapes = ProTrackShapes,
            content = content
        )
    }
}
