package com.hathway.projecttracker.data.repository

import com.hathway.projecttracker.data.db.ProjectDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsRepository(
    private val database: ProjectDatabase
) {
    private val settingsQueries get() = database.settingsEntityQueries

    private val initialDarkMode = try {
        settingsQueries.selectSetting("is_dark_mode").executeAsOneOrNull()?.toBoolean() ?: false
    } catch (_: Exception) {
        false
    }

    private val initialThemeMode = try {
        settingsQueries.selectSetting("theme_mode").executeAsOneOrNull() ?: "SYSTEM_DEFAULT"
    } catch (_: Exception) {
        "SYSTEM_DEFAULT"
    }

    private val initialNotifications = try {
        settingsQueries.selectSetting("notifications_enabled").executeAsOneOrNull()?.toBoolean() ?: true
    } catch (_: Exception) {
        true
    }

    private val _isDarkMode = MutableStateFlow(initialDarkMode)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _themeMode = MutableStateFlow(initialThemeMode)
    val themeMode: StateFlow<String> = _themeMode.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(initialNotifications)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    fun setThemeMode(mode: String) {
        _themeMode.update { mode }
        val darkMode = mode == "DARK"
        _isDarkMode.update { darkMode }
        try {
            settingsQueries.insertSetting("theme_mode", mode)
            settingsQueries.insertSetting("is_dark_mode", darkMode.toString())
        } catch (_: Exception) {}
    }

    fun toggleDarkMode(enabled: Boolean) {
        _isDarkMode.update { enabled }
        val mode = if (enabled) "DARK" else "LIGHT"
        _themeMode.update { mode }
        try {
            settingsQueries.insertSetting("is_dark_mode", enabled.toString())
            settingsQueries.insertSetting("theme_mode", mode)
        } catch (_: Exception) {}
    }

    fun toggleNotifications(enabled: Boolean) {
        _notificationsEnabled.update { enabled }
        try {
            settingsQueries.insertSetting("notifications_enabled", enabled.toString())
        } catch (_: Exception) {}
    }
}
