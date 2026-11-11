package com.hathway.projecttracker.data.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsRepository {
    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(true)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.update { enabled }
    }

    fun toggleNotifications(enabled: Boolean) {
        _notificationsEnabled.update { enabled }
    }
}
