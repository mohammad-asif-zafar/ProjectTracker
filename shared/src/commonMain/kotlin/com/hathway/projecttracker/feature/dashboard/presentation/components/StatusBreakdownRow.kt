package com.hathway.projecttracker.feature.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import com.hathway.projecttracker.core.ui.theme.SuccessGreen
import com.hathway.projecttracker.core.ui.theme.WarningOrange

@Composable
fun StatusBreakdownRow(
    color: Color,
    label: String,
    percentage: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(color)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Surface(
            shape = MaterialTheme.shapes.small, // Linked to your theme tokens
            color = color.copy(alpha = 0.15f)
        ) {
            Text(
                text = percentage,
                style = MaterialTheme.typography.bodySmall,
                color = color,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}

@Preview(name = "Light Theme", showBackground = true)
@Composable
fun StatusBreakdownRowLightPreview() {
    ProTrackTheme(darkTheme = false) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp)
        ) {
            StatusBreakdownRow(
                color = SuccessGreen,
                label = "Completed Tasks",
                percentage = "64%"
            )
        }
    }
}

@Preview(name = "Dark Theme", showBackground = true)
@Composable
fun StatusBreakdownRowDarkPreview() {
    ProTrackTheme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp)
        ) {
            StatusBreakdownRow(
                color = WarningOrange,
                label = "In Progress",
                percentage = "28%"
            )
        }
    }
}
