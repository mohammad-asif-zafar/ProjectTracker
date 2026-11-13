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
    val themeMode: String = "SYSTEM_DEFAULT",
    val notificationsEnabled: Boolean = true,
    val appVersion: String = "1.1 (ProTrack KMP)"
)

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    val uiState: StateFlow<SettingsUiState> = combine(
        settingsRepository.isDarkMode,
        settingsRepository.themeMode,
        settingsRepository.notificationsEnabled
    ) { darkMode, themeMode, notifications ->
        SettingsUiState(
            isDarkMode = darkMode,
            themeMode = themeMode,
            notificationsEnabled = notifications
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = SettingsUiState()
    )

    fun setThemeMode(mode: String) {
        settingsRepository.setThemeMode(mode)
    }

    fun toggleDarkMode(enabled: Boolean) {
        settingsRepository.toggleDarkMode(enabled)
    }

    fun toggleNotifications(enabled: Boolean) {
        settingsRepository.toggleNotifications(enabled)
    }
}
