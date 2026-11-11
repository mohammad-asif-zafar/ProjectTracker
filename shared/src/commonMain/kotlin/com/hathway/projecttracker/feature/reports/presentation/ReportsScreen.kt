package com.hathway.projecttracker.feature.reports.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hathway.projecttracker.core.ui.components.*
import org.jetbrains.compose.resources.painterResource
import projecttracker.shared.generated.resources.Res
import projecttracker.shared.generated.resources.home_bg

@Composable
fun ReportsScreen(
    viewModel: ReportsViewModel,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        LoadingIndicator(modifier = modifier)
        return
    }

    val progress = state.progress

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(Res.drawable.home_bg),
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
                .statusBarsPadding()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Text(
                    text = "Reports & Analytics",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
            }

            // Summary Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = "Completion Statistics",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        HorizontalDivider()
                        ReportStatRow("Overall Completion", "${progress.overallCompletion}%")
                        ReportStatRow("Total Projects", progress.totalProjects.toString())
                        ReportStatRow("Completed Projects", progress.completedProjects.toString())
                        ReportStatRow("In-Progress Projects", progress.inProgressProjects.toString())
                        ReportStatRow("Pending Projects", progress.pendingProjects.toString())
                        ReportStatRow("Total Subtasks", progress.totalSubtasks.toString())
                        ReportStatRow("Completed Subtasks", progress.completedSubtasks.toString())
                    }
                }
            }

            item {
                Text(
                    text = "Project-Wise Breakdown",
                    style = MaterialTheme.typography.headlineSmall,
                    color = Color.White
                )
            }

            if (state.projects.isEmpty()) {
                item {
                    EmptyState(
                        title = "No Data",
                        subtitle = "Create projects to view progress reports."
                    )
                }
            } else {
                items(state.projects, key = { it.id }) { project ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = MaterialTheme.shapes.medium,
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${project.icon} ${project.name}",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                StatusChip(status = project.status)
                            }
                            ProTrackProgressBar(progress = project.progress)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ReportStatRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}
