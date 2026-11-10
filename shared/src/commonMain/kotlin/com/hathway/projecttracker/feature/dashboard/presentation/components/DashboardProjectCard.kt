package com.hathway.projecttracker.feature.dashboard.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import com.hathway.projecttracker.core.ui.theme.SuccessGreen
import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.model.Subtask

@Composable
fun DashboardProjectCard(
    project: Project,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
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
                        .size(44.dp)
                        .clip(MaterialTheme.shapes.small)
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = project.icon.ifBlank { "📁" },
                        style = MaterialTheme.typography.headlineSmall
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = project.name,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "${project.shortName} • ${project.subtasks.size} Tasks",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    val pProg = (project.progress.coerceIn(0, 100)) / 100f
                    LinearProgressIndicator(
                        progress = { pProg },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = SuccessGreen,
                        trackColor = MaterialTheme.colorScheme.primaryContainer
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "${project.progress}%",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = ">",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

// --- Previews ---

private val mockProject = Project(
    id = "1",
    name = "Mobile App Revamp",
    shortName = "MAR",
    keywords = listOf("App"),
    description = "Mobile app redesign project",
    icon = "📱",
    progress = 75,
    status = ProjectStatus.IN_PROGRESS,
    subtasks = listOf(
        Subtask("s1", "1", "Task 1", "", 100, ProjectStatus.DONE),
        Subtask("s2", "1", "Task 2", "", 50, ProjectStatus.IN_PROGRESS)
    )
)

@Preview(name = "Light Mode Card", showBackground = true)
@Composable
fun DashboardProjectCardLightPreview() {
    ProTrackTheme(darkTheme = false) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp)
        ) {
            DashboardProjectCard(
                project = mockProject,
                onClick = {}
            )
        }
    }
}

@Preview(name = "Dark Mode Card", showBackground = true)
@Composable
fun DashboardProjectCardDarkPreview() {
    ProTrackTheme(darkTheme = true) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.padding(16.dp)
        ) {
            DashboardProjectCard(
                project = mockProject,
                onClick = {}
            )
        }
    }
}
