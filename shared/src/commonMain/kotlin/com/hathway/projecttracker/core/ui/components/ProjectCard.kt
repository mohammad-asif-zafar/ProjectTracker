package com.hathway.projecttracker.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.hathway.projecttracker.domain.model.Project

@Composable
fun ProjectCard(
    project: Project,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = project.icon.ifBlank { "📁" },
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                    Column {
                        Text(
                            text = project.name,
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = project.shortName,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
                StatusChip(status = project.status)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = project.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2
            )

            Spacer(modifier = Modifier.height(16.dp))

            ProTrackProgressBar(progress = project.progress, showLabel = true)

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${project.subtasks.size} Subtasks",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                val completed = project.subtasks.count { it.progress == 100 }
                Text(
                    text = "$completed Completed",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun ProjectCardPreviewLight() {
    com.hathway.projecttracker.core.ui.theme.ProTrackTheme(darkTheme = false) {
        Surface {
            ProjectCard(
                project = com.hathway.projecttracker.domain.model.Project(
                    id = "1",
                    name = "Little Spout",
                    shortName = "LittleSpout",
                    keywords = listOf("App"),
                    description = "Cross-platform mobile application.",
                    icon = "🌱",
                    progress = 50,
                    status = com.hathway.projecttracker.domain.model.ProjectStatus.IN_PROGRESS,
                    subtasks = listOf(com.hathway.projecttracker.domain.model.Subtask("s1", "1", "Test", "", 100, com.hathway.projecttracker.domain.model.ProjectStatus.DONE))
                ),
                onClick = {}
            )
        }
    }
}

@androidx.compose.ui.tooling.preview.Preview
@Composable
fun ProjectCardPreviewDark() {
    com.hathway.projecttracker.core.ui.theme.ProTrackTheme(darkTheme = true) {
        Surface {
            ProjectCard(
                project = com.hathway.projecttracker.domain.model.Project(
                    id = "1",
                    name = "Little Spout",
                    shortName = "LittleSpout",
                    keywords = listOf("App"),
                    description = "Cross-platform mobile application.",
                    icon = "🌱",
                    progress = 50,
                    status = com.hathway.projecttracker.domain.model.ProjectStatus.IN_PROGRESS,
                    subtasks = listOf(com.hathway.projecttracker.domain.model.Subtask("s1", "1", "Test", "", 100, com.hathway.projecttracker.domain.model.ProjectStatus.DONE))
                ),
                onClick = {}
            )
        }
    }
}
