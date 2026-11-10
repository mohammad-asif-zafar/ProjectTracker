package com.hathway.projecttracker.navigation

import androidx.compose.runtime.Composable

@Composable
expect fun ProTrackBackHandler(enabled: Boolean, onBack: () -> Unit)
