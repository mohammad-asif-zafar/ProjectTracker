package com.hathway.projecttracker.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable

@Composable
actual fun ProTrackBackHandler(enabled: Boolean, onBack: () -> Unit) {
    BackHandler(enabled = enabled, onBack = onBack)
}
