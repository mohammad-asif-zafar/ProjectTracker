package com.hathway.projecttracker.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*

sealed class NavigationItem(val route: String, val title: String, val iconEmoji: String) {
    data object Dashboard : NavigationItem(Screen.Dashboard.route, "Dashboard", "🏠")
    data object Projects : NavigationItem(Screen.Projects.route, "Projects", "📋")
    data object Reports : NavigationItem(Screen.Reports.route, "Reports", "📊")
    data object Settings : NavigationItem(Screen.Settings.route, "Settings", "⚙️")
}

@Composable
fun ResponsiveScaffold(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    content: @Composable (PaddingValues) -> Unit
) {
    val items = listOf(
        NavigationItem.Dashboard,
        NavigationItem.Projects,
        NavigationItem.Reports,
        NavigationItem.Settings
    )

    val showBottomNav = currentRoute in items.map { it.route }

    Scaffold(
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            if (showBottomNav) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface
                ) {
                    items.forEach { item ->
                        val selected = currentRoute == item.route
                        NavigationBarItem(
                            icon = { Text(item.iconEmoji, style = MaterialTheme.typography.bodyLarge) },
                            label = { Text(item.title) },
                            selected = selected,
                            onClick = {
                                if (!selected) {
                                    onNavigate(item.route)
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        content(paddingValues)
    }
}
