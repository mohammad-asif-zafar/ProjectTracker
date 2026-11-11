package com.hathway.projecttracker.feature.projects.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.hathway.projecttracker.core.ui.components.*
import com.hathway.projecttracker.domain.model.ProjectStatus
import org.jetbrains.compose.resources.painterResource
import projecttracker.shared.generated.resources.Res
import projecttracker.shared.generated.resources.home_bg

@Composable
fun ProjectsScreen(
    viewModel: ProjectsViewModel,
    onNavigateToProjectDetail: (String) -> Unit,
    onNavigateToAddProject: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Full screen background image (home_bg.png) filling edge-to-edge
        Image(
            painter = painterResource(Res.drawable.home_bg),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Subtle dark tint overlay for readability
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.15f))
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(16.dp)
        ) {
            // Title & Add Button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Projects",
                    style = MaterialTheme.typography.headlineLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color.White
                )
                IconButton(
                    onClick = onNavigateToAddProject,
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White.copy(alpha = 0.2f))
                ) {
                    Text(text = "+", style = MaterialTheme.typography.headlineMedium, color = Color.White)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar
            OutlinedTextField(
                value = state.searchQuery,
                onValueChange = { viewModel.onSearchQueryChanged(it) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search projects, keywords...") },
                shape = MaterialTheme.shapes.medium,
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                    focusedContainerColor = MaterialTheme.colorScheme.surface
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Status Filter Chips
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    FilterChip(
                        selected = state.selectedFilter == null,
                        onClick = { viewModel.onFilterSelected(null) },
                        label = { Text("All") }
                    )
                }
                item {
                    FilterChip(
                        selected = state.selectedFilter == ProjectStatus.IN_PROGRESS,
                        onClick = { viewModel.onFilterSelected(ProjectStatus.IN_PROGRESS) },
                        label = { Text("In Progress") }
                    )
                }
                item {
                    FilterChip(
                        selected = state.selectedFilter == ProjectStatus.DONE,
                        onClick = { viewModel.onFilterSelected(ProjectStatus.DONE) },
                        label = { Text("Done") }
                    )
                }
                item {
                    FilterChip(
                        selected = state.selectedFilter == ProjectStatus.PENDING,
                        onClick = { viewModel.onFilterSelected(ProjectStatus.PENDING) },
                        label = { Text("Pending") }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (state.isLoading) {
                LoadingIndicator()
            } else if (state.projects.isEmpty()) {
                EmptyState(
                    title = "No Projects Found",
                    subtitle = "Try adjusting your search query or filter."
                )
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(state.projects, key = { it.id }) { project ->
                        ProjectCard(
                            project = project,
                            onClick = { onNavigateToProjectDetail(project.id) }
                        )
                    }
                }
            }
        }
    }
}
