package com.hathway.projecttracker.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun EmptyState(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    action: (@Composable () -> Unit)? = null
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "📭",
            style = MaterialTheme.typography.headlineLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        if (action != null) {
            Spacer(modifier = Modifier.height(24.dp))
            action()
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun EmptyStatePreviewLight() {
    com.hathway.projecttracker.core.ui.theme.ProTrackTheme(darkTheme = false) {
        androidx.compose.material3.Surface {
            EmptyState(title = "No Projects", subtitle = "Create your first project to get started.")
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun EmptyStatePreviewDark() {
    com.hathway.projecttracker.core.ui.theme.ProTrackTheme(darkTheme = true) {
        androidx.compose.material3.Surface {
            EmptyState(title = "No Projects", subtitle = "Create your first project to get started.")
        }
    }
}
