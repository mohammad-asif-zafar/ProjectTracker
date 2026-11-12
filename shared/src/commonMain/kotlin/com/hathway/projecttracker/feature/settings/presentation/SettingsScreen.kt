package com.hathway.projecttracker.feature.settings.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import com.hathway.projecttracker.core.ui.theme.rememberBackgroundPainter

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    onBackClick: () -> Unit,
    onNavigateToBackup: () -> Unit,
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    SettingsContent(
        uiState = state,
        onBackClick = onBackClick,
        onThemeModeSelected = viewModel::setThemeMode,
        onNotificationsChanged = viewModel::toggleNotifications,
        onNavigateToBackup = onNavigateToBackup,
        onNavigateToAbout = onNavigateToAbout,
        modifier = modifier
    )
}

@Composable
fun SettingsContent(
    uiState: SettingsUiState,
    onBackClick: () -> Unit,
    onThemeModeSelected: (String) -> Unit,
    onNotificationsChanged: (Boolean) -> Unit,
    onNavigateToBackup: () -> Unit,
    onNavigateToAbout: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showDeveloperDialog by remember { mutableStateOf(false) }
    var showPolicyDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        // Full screen background matching overall layout design system
        Image(
            painter = rememberBackgroundPainter(),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.15f))
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize().statusBarsPadding(),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header: Arrow navigation action bar layout matching screenshot
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        IconButton(
                            onClick = onBackClick,
                            modifier = Modifier.size(40.dp)
                                .background(Color.White.copy(alpha = 0.2f), shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                                contentDescription = "Go Back",
                                tint = Color.White,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "Settings",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold, fontSize = 22.sp
                                ),
                                color = Color.White
                            )
                            Text(
                                text = "Customize your app experience",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f)
                            )
                        }
                    }
                }
            }

            // Category Group 1: Appearance Block
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Palette,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Appearance",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White.copy(alpha = 0.95f)
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Dark Mode Option Row
                            SettingsThemeOptionRow(
                                title = "Dark Mode",
                                icon = Icons.Rounded.DarkMode,
                                isSelected = uiState.themeMode == "DARK",
                                onClick = { onThemeModeSelected("DARK") }
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                                modifier = Modifier.padding(start = 52.dp)
                            )
                            // Light Mode Option Row
                            SettingsThemeOptionRow(
                                title = "Light Mode",
                                icon = Icons.Rounded.LightMode,
                                isSelected = uiState.themeMode == "LIGHT",
                                onClick = { onThemeModeSelected("LIGHT") }
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                                modifier = Modifier.padding(start = 52.dp)
                            )
                            // System Default Option Row
                            SettingsThemeOptionRow(
                                title = "System Default",
                                icon = Icons.Rounded.Settings,
                                isSelected = uiState.themeMode == "SYSTEM_DEFAULT",
                                onClick = { onThemeModeSelected("SYSTEM_DEFAULT") }
                            )
                        }
                    }
                }
            }

            // Category Group 2: General Settings Block
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Tune,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "General",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White.copy(alpha = 0.95f)
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            // Toggle Notifications Row
                            SettingsToggleRow(
                                title = "Notifications",
                                subtitle = "Receive project and task updates",
                                icon = Icons.Rounded.NotificationsNone,
                                isChecked = uiState.notificationsEnabled,
                                onCheckedChange = onNotificationsChanged
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                                modifier = Modifier.padding(start = 52.dp)
                            )
                            // Backup Entry Link Row
                            SettingsNavigationRow(
                                title = "Backup & Sync",
                                subtitle = "Keep your data safe and in sync",
                                icon = Icons.Rounded.CloudSync,
                                onClick = onNavigateToBackup
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                                modifier = Modifier.padding(start = 52.dp)
                            )
                            // About Application info link Row
                            SettingsNavigationRow(
                                title = "About",
                                subtitle = "App version, release notes and more",
                                icon = Icons.Rounded.Info,
                                onClick = onNavigateToAbout
                            )
                        }
                    }
                }
            }

            // Category Group 3: Information Block
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(start = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Security,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "Information",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.White.copy(alpha = 0.95f)
                        )
                    }

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            SettingsNavigationRow(
                                title = "Privacy",
                                subtitle = "Manage your privacy settings",
                                icon = Icons.Rounded.Lock,
                                onClick = { showPrivacyDialog = true }
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                                modifier = Modifier.padding(start = 52.dp)
                            )
                            SettingsNavigationRow(
                                title = "Developer Information",
                                subtitle = "App developer details and contact",
                                icon = Icons.Rounded.Code,
                                onClick = { showDeveloperDialog = true }
                            )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f),
                                modifier = Modifier.padding(start = 52.dp)
                            )
                            SettingsNavigationRow(
                                title = "Data Privacy Policy",
                                subtitle = "How we collect, use and protect your data",
                                icon = Icons.Rounded.Description,
                                onClick = { showPolicyDialog = true }
                            )
                        }
                    }
                }
            }
        }

        if (showPrivacyDialog) {
            AlertDialog(
                onDismissRequest = { showPrivacyDialog = false },
                title = { Text("Privacy Settings") },
                text = { Text("ProTrack operates 100% offline. All project files, tasks, and history are stored securely in your device's local SQLite database with zero cloud telemetry.") },
                confirmButton = {
                    TextButton(onClick = { showPrivacyDialog = false }) {
                        Text("OK", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        if (showDeveloperDialog) {
            AlertDialog(
                onDismissRequest = { showDeveloperDialog = false },
                title = { Text("Developer Information") },
                text = { Text("ProTrack KMP\nBuilt with Kotlin Multiplatform, Compose Multiplatform, and SQLDelight.\nVersion: 1.0.0") },
                confirmButton = {
                    TextButton(onClick = { showDeveloperDialog = false }) {
                        Text("OK", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }

        if (showPolicyDialog) {
            AlertDialog(
                onDismissRequest = { showPolicyDialog = false },
                title = { Text("Data Privacy Policy") },
                text = { Text("Your privacy is fully protected. Because ProTrack operates entirely offline without external servers or cloud accounts, your data never leaves your device.") },
                confirmButton = {
                    TextButton(onClick = { showPolicyDialog = false }) {
                        Text("OK", fontWeight = FontWeight.Bold)
                    }
                }
            )
        }
    }
}

@Composable
private fun SettingsThemeOptionRow(
    title: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val rowModifier = if (isSelected) {
        Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)).clickable(onClick = onClick)
    } else {
        Modifier.fillMaxWidth().clickable(onClick = onClick)
    }

    Row(
        modifier = rowModifier.padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        RadioButton(
            selected = isSelected,
            onClick = onClick,
            colors = RadioButtonDefaults.colors(
                selectedColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun SettingsToggleRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Switch(
            checked = isChecked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun SettingsNavigationRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(22.dp)
            )
            Column {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(
            imageVector = Icons.AutoMirrored.Rounded.KeyboardArrowRight,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
            modifier = Modifier.size(20.dp)
        )
    }
}

// --- Preview Engine Blocks ---
@Preview(name = "Settings Canvas Light", showSystemUi = true)
@Composable
fun SettingsScreenLightPreview() {
    val mockState = SettingsUiState(isDarkMode = false, themeMode = "SYSTEM_DEFAULT", notificationsEnabled = true)
    ProTrackTheme(darkTheme = false) {
        SettingsContent(
            uiState = mockState,
            onBackClick = {},
            onThemeModeSelected = {},
            onNotificationsChanged = {},
            onNavigateToBackup = {},
            onNavigateToAbout = {})
    }
}

@Preview(name = "Settings Canvas Dark", showSystemUi = true)
@Composable
fun SettingsScreenDarkPreview() {
    val mockState = SettingsUiState(isDarkMode = true, themeMode = "DARK", notificationsEnabled = false)
    ProTrackTheme(darkTheme = true) {
        SettingsContent(
            uiState = mockState,
            onBackClick = {},
            onThemeModeSelected = {},
            onNotificationsChanged = {},
            onNavigateToBackup = {},
            onNavigateToAbout = {},
            modifier = Modifier)
    }
}
