package com.hathway.projecttracker.feature.reports.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AssignmentTurnedIn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hathway.projecttracker.core.ui.components.*
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import com.hathway.projecttracker.core.ui.theme.SuccessGreen
import com.hathway.projecttracker.core.ui.theme.rememberBackgroundPainter
import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectProgress
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.feature.dashboard.presentation.components.OverviewSummaryCard

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        LoadingIndicator(modifier = modifier.fillMaxSize())
        return
    }

    ReportsContent(
        progress = state.progress,
        projects = state.projects,
        modifier = modifier
    )
}

@Composable
fun ReportsContent(
    progress: ProjectProgress,
    projects: List<Project>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = rememberBackgroundPainter(),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.15f))
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 32.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Screen Title
            item {
                Column(modifier = Modifier.padding(horizontal = 4.dp)) {
                    Text(
                        text = "Reports & Analytics",
                        style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Track overall project health and subtask velocity",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            // Reused Animated Circular Progress & Breakdown Card
            item {
                OverviewSummaryCard(progress = progress)
            }

            // Subtask Completion Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.AssignmentTurnedIn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(22.dp)
                            )
                            Text(
                                text = "Subtask Velocity",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

                        ReportStatRow(
                            label = "Total Subtasks",
                            value = progress.totalSubtasks.toString()
                        )
                        ReportStatRow(
                            label = "Completed Subtasks",
                            value = progress.completedSubtasks.toString(),
                            valueColor = SuccessGreen
                        )
                        ReportStatRow(
                            label = "Remaining Subtasks",
                            value = (progress.totalSubtasks - progress.completedSubtasks).coerceAtLeast(0).toString()
                        )
                    }
                }
            }

            // Section Header
            item {
                Text(
                    text = "Project-Wise Breakdown (${projects.size})",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = Color.White,
                    modifier = Modifier.padding(top = 4.dp, start = 4.dp)
                )
            }

            // Project List
            if (projects.isEmpty()) {
                item {
                    EmptyState(
                        title = "No Data Available",
                        subtitle = "Create projects to view progress reports."
                    )
                }
            } else {
                items(projects, key = { it.id }) { project ->
                    ReportProjectItemCard(project = project)
                }
            }
        }
    }
}

@Composable
private fun ReportProjectItemCard(project: Project) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
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
                            .clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        ProjectIconView(
                            iconStr = project.icon,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(
                            text = project.name,
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${project.subtasks.size} Subtasks • ${project.progress}%",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                StatusChip(status = project.status)
            }

            ProTrackProgressBar(progress = project.progress)
        }
    }
}

@Composable
fun ReportStatRow(
    label: String,
    value: String,
    valueColor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
            color = valueColor
        )
    }
}


// --- Previews ---
private val mockReportProgress = ProjectProgress(
    totalProjects = 4,
    completedProjects = 1,
    inProgressProjects = 2,
    pendingProjects = 1,
    totalSubtasks = 12,
    completedSubtasks = 7,
    overallCompletion = 58
)
private val mockReportProjects = listOf(
    Project(
        id = "1",
        name = "Little Spout",
        shortName = "LS",
        keywords = listOf("android", "ios"),
        description = "Kids learning app",
        icon = "ai",
        progress = 100,
        status = ProjectStatus.DONE
    ),
    Project(
        id = "2",
        name = "CMM Project Tracker",
        shortName = "CMM",
        keywords = listOf("kmp", "compose"),
        description = "Multiplatform tracker",
        icon = "code",
        progress = 65,
        status = ProjectStatus.IN_PROGRESS
    ),
    Project(
        id = "3",
        name = "Store Asset Pack",
        shortName = "SAP",
        keywords = listOf("design"),
        description = "Screenshots and icons",
        icon = "googleplay",
        progress = 0,
        status = ProjectStatus.PENDING
    )
)
@Preview(name = "Reports Light Mode", showSystemUi = true)
@Composable
fun ReportsScreenLightPreview() {
    ProTrackTheme(darkTheme = false) {
        ReportsContent(
            progress = mockReportProgress,
            projects = mockReportProjects
        )
    }
}
@Preview(name = "Reports Dark Mode", showSystemUi = true)
@Composable
fun ReportsScreenDarkPreview() {
    ProTrackTheme(darkTheme = true) {
        ReportsContent(
            progress = mockReportProgress,
            projects = mockReportProjects
        )
    }
}