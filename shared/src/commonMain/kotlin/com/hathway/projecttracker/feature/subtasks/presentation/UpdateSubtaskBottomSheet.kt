package com.hathway.projecttracker.feature.subtasks.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hathway.projecttracker.core.ui.components.TaskBrandIcon
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import com.hathway.projecttracker.core.ui.theme.SuccessGreen
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.model.Subtask

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateSubtaskBottomSheet(
    subtask: Subtask,
    onDismissRequest: () -> Unit,
    onUpdateClick: (progress: Int, status: ProjectStatus) -> Unit,
    onDelete: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    // 1. Maintain interactive internal slider and status state references
    var currentProgress by remember(subtask.id) { mutableFloatStateOf(subtask.progress.toFloat()) }
    var currentStatus by remember(subtask.id) { mutableStateOf(subtask.status) }
    var showDeleteSubtaskDialog by remember { mutableStateOf(false) }

    // Automatically update the status state if the user sets progress to 0% or 100%
    LaunchedEffect(currentProgress) {
        when (currentProgress.toInt()) {
            100 -> currentStatus = ProjectStatus.DONE
            0 -> currentStatus = ProjectStatus.PENDING
            else -> if (currentStatus == ProjectStatus.DONE || currentStatus == ProjectStatus.PENDING) {
                currentStatus = ProjectStatus.IN_PROGRESS
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        modifier = modifier,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = {
            BottomSheetDefaults.DragHandle(
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
            )
        }) {
        Column(
            modifier = Modifier.fillMaxWidth().navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            // Header Action Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Update Subtask", style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold, fontSize = 22.sp
                    ), color = MaterialTheme.colorScheme.onSurface
                )
                IconButton(onClick = onDismissRequest) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = "Close sheet",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Task Name Container with integrated Brand Asset
            Row(
                modifier = Modifier.fillMaxWidth().clip(MaterialTheme.shapes.medium)
                    .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                val cleanKey = subtask.title.lowercase()
                val resolvedIconKey = when {
                    "google play" in cleanKey || "play store" in cleanKey -> "googleplay"
                    "app store" in cleanKey -> "appstore"
                    "apple" in cleanKey || "ios" in cleanKey -> "apple"
                    else -> "assignment"
                }

                TaskBrandIcon(iconKey = resolvedIconKey, modifier = Modifier.size(24.dp))

                Text(
                    text = subtask.title,
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            // Progress Slider Block
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Progress",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Surface(
                        shape = MaterialTheme.shapes.small,
                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        modifier = Modifier.padding(bottom = 4.dp)
                    ) {
                        Text(
                            text = "${currentProgress.toInt()}%",
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                Slider(
                    value = currentProgress,
                    onValueChange = { currentProgress = it },
                    valueRange = 0f..100f,
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
                    )
                )

                // Precise Slider Metric Indicators
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "0%",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "50%",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "100%",
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Status Segments Group Selection Block
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Status",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Segment Option 1: Done Status
                    StatusSegmentButton(
                        label = "Done",
                        icon = Icons.Rounded.CheckCircle,
                        iconColor = SuccessGreen,
                        isSelected = currentStatus == ProjectStatus.DONE,
                        selectedBorderColor = SuccessGreen,
                        selectedBgColor = SuccessGreen.copy(alpha = 0.08f),
                        onClick = {
                            currentStatus = ProjectStatus.DONE
                            currentProgress = 100f
                        },
                        modifier = Modifier.weight(1f)
                    )

                    // Segment Option 2: In Progress Status
                    StatusSegmentButton(
                        label = "In Progress",
                        icon = Icons.Rounded.HourglassEmpty,
                        iconColor = MaterialTheme.colorScheme.primary,
                        isSelected = currentStatus == ProjectStatus.IN_PROGRESS,
                        selectedBorderColor = MaterialTheme.colorScheme.primary,
                        selectedBgColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                        onClick = {
                            currentStatus = ProjectStatus.IN_PROGRESS
                            if (currentProgress == 0f || currentProgress == 100f) currentProgress =
                                50f
                        },
                        modifier = Modifier.weight(1.3f)
                    )

                    // Segment Option 3: Pending Status
                    StatusSegmentButton(
                        label = "Pending",
                        icon = Icons.Rounded.RadioButtonUnchecked,
                        iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        isSelected = currentStatus == ProjectStatus.PENDING,
                        selectedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        selectedBgColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f),
                        onClick = {
                            currentStatus = ProjectStatus.PENDING
                            currentProgress = 0f
                        },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))

            // Action Control Row Block
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (onDelete != null) {
                    IconButton(
                        onClick = { showDeleteSubtaskDialog = true },
                        modifier = Modifier.size(48.dp)
                            .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f), shape = MaterialTheme.shapes.medium)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Delete,
                            contentDescription = "Delete Subtask",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
                OutlinedButton(
                    onClick = onDismissRequest,
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.onSurfaceVariant)
                ) {
                    Text(
                        text = "Cancel",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Button(
                    onClick = { onUpdateClick(currentProgress.toInt(), currentStatus) },
                    modifier = Modifier.weight(1f).height(48.dp),
                    shape = MaterialTheme.shapes.medium,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Update",
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }

    if (showDeleteSubtaskDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteSubtaskDialog = false },
            title = { Text("Delete Subtask") },
            text = { Text("Are you sure you want to delete \"${subtask.title}\"?") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDeleteSubtaskDialog = false
                        onDelete?.invoke()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Delete", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteSubtaskDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
private fun StatusSegmentButton(
    label: String,
    icon: ImageVector,
    iconColor: Color,
    isSelected: Boolean,
    selectedBorderColor: Color,
    selectedBgColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val borderModifier = if (isSelected) {
        Modifier.border(1.5.dp, selectedBorderColor, MaterialTheme.shapes.medium)
    } else {
        Modifier.border(
            1.dp,
            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f),
            MaterialTheme.shapes.medium
        )
    }
    Box(
        modifier = modifier.then(borderModifier).clip(MaterialTheme.shapes.medium)
            .background(if (isSelected) selectedBgColor else Color.Transparent)
            .clickable(onClick = onClick).padding(vertical = 10.dp, horizontal = 4.dp),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.wrapContentSize()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                color = if (isSelected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}

// --- Previews ---
private val mockSampleSubtask = Subtask(
    id = "1",
    projectId = "proj_1",
    title = "Live to google play store",
    description = "",
    progress = 100,
    status = ProjectStatus.DONE
)

@Preview(name = "Sheet Light Setup", showBackground = true)
@Composable
fun UpdateSubtaskBottomSheetLightPreview() {
    ProTrackTheme(darkTheme = false) {
        UpdateSubtaskBottomSheet(
            subtask = mockSampleSubtask,
            onDismissRequest = {},
            onUpdateClick = { _, _ -> },
            onDelete = {})
    }
}
