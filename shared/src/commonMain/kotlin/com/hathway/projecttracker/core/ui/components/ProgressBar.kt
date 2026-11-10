package com.hathway.projecttracker.core.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun ProTrackProgressBar(
    progress: Int, // 0 to 100
    modifier: Modifier = Modifier,
    showLabel: Boolean = true
) {
    val floatProgress = (progress.coerceIn(0, 100)) / 100f
    Column(modifier = modifier) {
        if (showLabel) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Progress",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "$progress%",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(4.dp))
        }
        LinearProgressIndicator(
            progress = { floatProgress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(RoundedCornerShape(4.dp)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primaryContainer
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun ProgressBarPreviewLight() {
    com.hathway.projecttracker.core.ui.theme.ProTrackTheme(darkTheme = false) {
        androidx.compose.material3.Surface {
            ProTrackProgressBar(progress = 75)
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun ProgressBarPreviewDark() {
    com.hathway.projecttracker.core.ui.theme.ProTrackTheme(darkTheme = true) {
        androidx.compose.material3.Surface {
            ProTrackProgressBar(progress = 75)
        }
    }
}
