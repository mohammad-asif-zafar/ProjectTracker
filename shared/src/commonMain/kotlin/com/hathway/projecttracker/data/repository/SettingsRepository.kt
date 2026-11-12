package com.hathway.projecttracker.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsRepository {
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _themeMode = MutableStateFlow("SYSTEM_DEFAULT") // "DARK", "LIGHT", "SYSTEM_DEFAULT"
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    fun setThemeMode(mode: String) {
        _themeMode.update { mode }
        when (mode) {
            "DARK" -> _isDarkMode.update { true }
            "LIGHT" -> _isDarkMode.update { false }
            "SYSTEM_DEFAULT" -> {}
        }
    }

    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.update { enabled }
        _themeMode.update { if (enabled) "DARK" else "LIGHT" }
    }

    fun toggleNotifications(enabled: Boolean) {
        _notificationsEnabled.update { enabled }
    }
}
