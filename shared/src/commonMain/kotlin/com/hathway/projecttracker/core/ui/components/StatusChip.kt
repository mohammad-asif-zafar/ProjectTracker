package com.hathway.projecttracker.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.core.ui.theme.SuccessGreen
import com.hathway.projecttracker.core.ui.theme.WarningOrange
import com.hathway.projecttracker.core.ui.theme.PrimaryBlue

@Composable
fun StatusChip(
    status: ProjectStatus,
    modifier: Modifier = Modifier
) {
    val (backgroundColor, textColor, label) = when (status) {
        ProjectStatus.DONE -> Triple(SuccessGreen.copy(alpha = 0.15f), SuccessGreen, "Done")
        ProjectStatus.IN_PROGRESS -> Triple(WarningOrange.copy(alpha = 0.15f), WarningOrange, "In Progress")
        ProjectStatus.PENDING -> Triple(PrimaryBlue.copy(alpha = 0.15f), PrimaryBlue, "Pending")
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(backgroundColor)
            .padding(horizontal = 12.dp, vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun StatusChipPreviewLight() {
    com.hathway.projecttracker.core.ui.theme.ProTrackTheme(darkTheme = false) {
        androidx.compose.material3.Surface {
            StatusChip(status = com.hathway.projecttracker.domain.model.ProjectStatus.IN_PROGRESS)
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun StatusChipPreviewDark() {
    com.hathway.projecttracker.core.ui.theme.ProTrackTheme(darkTheme = true) {
        androidx.compose.material3.Surface {
            StatusChip(status = com.hathway.projecttracker.domain.model.ProjectStatus.IN_PROGRESS)
        }
    }
}
