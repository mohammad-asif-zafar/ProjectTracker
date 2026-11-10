package com.hathway.projecttracker.navigation

import androidx.compose.runtime.Composable

@Composable
actual fun ProTrackBackHandler(enabled: Boolean, onBack: () -> Unit) {
    // No-op on iOS
}
