package com.hathway.projecttracker.feature.dashboard.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hathway.projecttracker.core.ui.components.EmptyState
import com.hathway.projecttracker.core.ui.components.LoadingIndicator
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectProgress
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.model.Subtask
import com.hathway.projecttracker.feature.components.ProTrackButton
import com.hathway.projecttracker.feature.dashboard.presentation.components.DashboardProjectCard
import com.hathway.projecttracker.feature.dashboard.presentation.components.OverviewSummaryCard
import org.jetbrains.compose.resources.painterResource
import projecttracker.shared.generated.resources.Res
import projecttracker.shared.generated.resources.home_bg

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToProjectDetail: (String) -> Unit,
    onNavigateToAddProject: () -> Unit,
    onNavigateToProjects: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    DashboardContent(
        uiState = uiState,
        onNavigateToProjectDetail = onNavigateToProjectDetail,
        onNavigateToAddProject = onNavigateToAddProject,
        onNavigateToProjects = onNavigateToProjects,
        modifier = modifier
    )
}

@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    onNavigateToProjectDetail: (String) -> Unit,
    onNavigateToAddProject: () -> Unit,
    onNavigateToProjects: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading) {
        LoadingIndicator(modifier = modifier.fillMaxSize())
        return
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Full screen background image (home_bg.png) filling edge-to-edge
        Image(
            painter = painterResource(Res.drawable.home_bg),
            contentDescription = "Dashboard Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Subtle dark tint overlay for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.15f))
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top Header Layout with status bar padding
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "CMM Project Tracker",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White
                        )

                        IconButton(
                            onClick = { },
                            modifier = Modifier.background(Color.White.copy(alpha = 0.2f), shape = CircleShape)
                        ) {
                            Text(text = "🔔", style = MaterialTheme.typography.bodyLarge)
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Track, manage, and update your app projects",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.85f)
                    )
                }
            }

            // Overview Summary Card Container
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    OverviewSummaryCard(progress = uiState.progress)
                }
            }

            // Projects Subsection Management Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Projects (${uiState.projects.size})",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color.White
                    )
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = onNavigateToProjects) {
                            Text(text = "🔍", style = MaterialTheme.typography.bodyLarge)
                        }

                        ProTrackButton(
                            onClick = onNavigateToAddProject,
                            text = "Project",
                            iconSymbol = "+"
                        )
                    }
                }
            }

            // Conditional Layout for Project Tracking Items
            if (uiState.projects.isEmpty()) {
                item {
                    Box(modifier = Modifier.padding(16.dp)) {
                        EmptyState(
                            title = "No Projects Yet",
                            subtitle = "Tap the + Project button above to create your first project."
                        )
                    }
                }
            } else {
                items(uiState.projects, key = { it.id }) { project ->
                    Box(modifier = Modifier.padding(horizontal = 16.dp)) {
                        DashboardProjectCard(
                            project = project,
                            onClick = { onNavigateToProjectDetail(project.id) }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

// --- Preview Configurations ---

private val mockState = DashboardUiState(
    isLoading = false,
    projects = listOf(
        Project(
            id = "proj-1",
            name = "Little Spout",
            shortName = "LS",
            keywords = listOf("App"),
            description = "Eco tracking mobile application.",
            icon = "🌱",
            progress = 25,
            status = ProjectStatus.IN_PROGRESS,
            subtasks = listOf(
                Subtask("sub-1", "proj-1", "Google Play Store", "", 100, ProjectStatus.DONE),
                Subtask("sub-2", "proj-1", "Android Tablet", "", 0, ProjectStatus.PENDING)
            )
        ),
        Project(
            id = "proj-2",
            name = "Smart Home Hub",
            shortName = "HomeHub",
            keywords = listOf("IoT"),
            description = "IoT automation hub.",
            icon = "🏠",
            progress = 100,
            status = ProjectStatus.DONE,
            subtasks = listOf(
                Subtask("sub-3", "proj-2", "MQTT Protocol", "", 100, ProjectStatus.DONE)
            )
        )
    ),
    progress = ProjectProgress(
        totalProjects = 2,
        completedProjects = 1,
        inProgressProjects = 1,
        pendingProjects = 0,
        totalSubtasks = 3,
        completedSubtasks = 2,
        overallCompletion = 62
    )
)

@Preview(name = "Light Full Screen Dashboard", showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenLightPreview() {
    ProTrackTheme(darkTheme = false) {
        DashboardContent(
            uiState = mockState,
            onNavigateToAddProject = {},
            onNavigateToProjectDetail = {},
            onNavigateToProjects = {}
        )
    }
}

@Preview(name = "Dark Full Screen Dashboard", showBackground = true, showSystemUi = true)
@Composable
fun DashboardScreenDarkPreview() {
    ProTrackTheme(darkTheme = true) {
        DashboardContent(
            uiState = mockState,
            onNavigateToAddProject = {},
            onNavigateToProjectDetail = {},
            onNavigateToProjects = {}
        )
    }
}
