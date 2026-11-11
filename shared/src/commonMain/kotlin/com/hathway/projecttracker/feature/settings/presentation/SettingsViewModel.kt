package com.hathway.projecttracker.feature.settings.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hathway.projecttracker.data.repository.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class SettingsUiState(
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val appVersion: String = "1.0.0 (ProTrack KMP)"
)

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val uiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.isDarkMode,
        settingsRepository.notificationsEnabled
    ) { darkMode, notifications ->
        SettingsUiState(
            isDarkMode = darkMode,
            notificationsEnabled = notifications
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun toggleDarkMode(enabled: Boolean) {
        settingsRepository.toggleDarkMode(enabled)
    }

    fun toggleNotifications(enabled: Boolean) {
        settingsRepository.toggleNotifications(enabled)
    }
}
