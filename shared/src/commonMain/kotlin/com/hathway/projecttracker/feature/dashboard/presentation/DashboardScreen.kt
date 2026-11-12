package com.hathway.projecttracker.feature.dashboard.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hathway.projecttracker.core.ui.components.EmptyState
import com.hathway.projecttracker.core.ui.components.LoadingIndicator
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import com.hathway.projecttracker.core.ui.theme.rememberBackgroundPainter
import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectProgress
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.model.Subtask
import com.hathway.projecttracker.feature.components.ProTrackButton
import com.hathway.projecttracker.feature.dashboard.presentation.components.DashboardProjectCard
import com.hathway.projecttracker.feature.dashboard.presentation.components.OverviewSummaryCard

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    onNavigateToProjectDetail: (String) -> Unit,
    onNavigateToAddProject: () -> Unit,
    onNavigateToProjects: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    DashboardContent(
        uiState = uiState,
        onNavigateToProjectDetail = onNavigateToProjectDetail,
        onNavigateToAddProject = onNavigateToAddProject,
        onNavigateToProjects = onNavigateToProjects,
        onNavigateToNotifications = onNavigateToNotifications,
        modifier = modifier
    )
}

@Composable
fun DashboardContent(
    uiState: DashboardUiState,
    onNavigateToProjectDetail: (String) -> Unit,
    onNavigateToAddProject: () -> Unit,
    onNavigateToProjects: () -> Unit,
    onNavigateToNotifications: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading) {
        LoadingIndicator(modifier = modifier.fillMaxSize())
        return
    }

    Box(modifier = modifier.fillMaxSize()) {
        // Full screen background image (home_bg.png) filling edge-to-edge
        Image(
            painter = rememberBackgroundPainter(),
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
            verticalArrangement = Arrangement.spacedBy(0.8.dp)
        ) {
            // Top Header Layout
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
                            onClick = onNavigateToNotifications,
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
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color.White
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Surface avoids IconButton's forced 48.dp minimum layout bounds
                        Surface(
                            onClick = onNavigateToProjects,
                            modifier = Modifier.size(38.dp),
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Rounded.Search,
                                    contentDescription = "Search Projects",
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        ProTrackButton(
                            onClick = onNavigateToAddProject,
                            text = "Project",
                            iconSymbol = "+",
                            modifier = Modifier.height(38.dp)
                        )
                    }
                }
            }



            // Project Cards List streams
            if (uiState.projects.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        EmptyState(
                            title = "No Projects Yet",
                            subtitle = "Tap '+ Project' above to create your first tracking workflow.",
                            action = {
                                ProTrackButton(
                                    onClick = onNavigateToAddProject,
                                    text = "Create Project",
                                    iconSymbol = "+"
                                )
                            }
                        )
                    }
                }
            } else {
                items(uiState.projects.take(5), key = { it.id }) { project ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
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

@Preview(name = "Dashboard Light Mode", showSystemUi = true)
@Composable
fun DashboardScreenLightPreview() {
    val mockState = DashboardUiState(
        isLoading = false,
        projects = listOf(
            Project(
                id = "1",
                name = "Little Spout",
                shortName = "LittleSpout",
                keywords = listOf("Mobile", "App"),
                description = "Cross-platform mobile application.",
                icon = "🌱",
                progress = 25,
                status = ProjectStatus.IN_PROGRESS,
                subtasks = listOf(Subtask("s1", "1", "Setup", "", 100, ProjectStatus.DONE))
            )
        ),
        progress = ProjectProgress(1, 0, 1, 0, 1, 1, 25)
    )
    ProTrackTheme(darkTheme = false) {
        DashboardContent(
            uiState = mockState,
            onNavigateToProjectDetail = {},
            onNavigateToAddProject = {},
            onNavigateToProjects = {},
            onNavigateToNotifications = {}
        )
    }
}

@Preview(name = "Dashboard Dark Mode", showSystemUi = true)
@Composable
fun DashboardScreenDarkPreview() {
    val mockState = DashboardUiState(
        isLoading = false,
        projects = listOf(
            Project(
                id = "2",
                name = "Smart Home Hub",
                shortName = "HomeHub",
                keywords = listOf("IoT"),
                description = "Centralized IoT control hub.",
                icon = "🏠",
                progress = 100,
                status = ProjectStatus.DONE,
                subtasks = listOf(Subtask("s2", "2", "MQTT", "", 100, ProjectStatus.DONE))
            )
        ),
        progress = ProjectProgress(1, 1, 0, 0, 1, 1, 100)
    )
    ProTrackTheme(darkTheme = true) {
        DashboardContent(
            uiState = mockState,
            onNavigateToProjectDetail = {},
            onNavigateToAddProject = {},
            onNavigateToProjects = {},
            onNavigateToNotifications = {}
        )
    }
}
