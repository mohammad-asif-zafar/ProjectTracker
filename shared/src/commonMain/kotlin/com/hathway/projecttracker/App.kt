package com.hathway.projecttracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.hathway.projecttracker.di.appModule
import com.hathway.projecttracker.navigation.ProTrackApp
import org.koin.core.context.startKoin

@Composable
fun App() {
    remember {
        try {
            startKoin {
                modules(appModule)
            }
        } catch (_: Throwable) {
            // Koin already started
        }
    }
    ProTrackApp()
}
