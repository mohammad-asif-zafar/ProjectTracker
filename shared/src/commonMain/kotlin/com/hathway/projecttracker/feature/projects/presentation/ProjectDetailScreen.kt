package com.hathway.projecttracker.feature.projects.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hathway.projecttracker.core.ui.components.*
import com.hathway.projecttracker.domain.model.Subtask

@Composable
fun ProjectDetailScreen(
    viewModel: ProjectDetailViewModel,
    onBack: () -> Unit,
    onEditProject: (String) -> Unit,
    onAddSubtask: (String) -> Unit,
    onEditSubtask: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    if (state.isLoading) {
        LoadingIndicator(modifier = modifier)
        return
    }

    val project = state.project
    if (project == null) {
        EmptyState(
            title = "Project Not Found",
            subtitle = "The project may have been deleted.",
            action = {
                PrimaryButton(text = "Go Back", onClick = onBack)
            }
        )
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(onClick = onBack) {
                    Text("← Back")
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = { onEditProject(project.id) }) {
                        Text("Edit")
                    }
                    TextButton(
                        onClick = { viewModel.deleteProject(onBack) },
                        colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                    ) {
                        Text("Delete")
                    }
                }
            }
        }

        // Project Info Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${project.icon} ${project.name}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        StatusChip(status = project.status)
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = project.description,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ProTrackProgressBar(progress = project.progress)

                    Spacer(modifier = Modifier.height(12.dp))

                    val doneCount = project.subtasks.count { it.progress == 100 }
                    val inProgressCount = project.subtasks.count { it.progress in 1..99 }
                    val pendingCount = project.subtasks.count { it.progress == 0 }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "Done: $doneCount", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                        Text(text = "In Progress: $inProgressCount", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.secondary)
                        Text(text = "Pending: $pendingCount", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        // Subtasks Header
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Subtasks (${project.subtasks.size})",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground
                )
                PrimaryButton(
                    text = "+ Add",
                    onClick = { onAddSubtask(project.id) },
                    modifier = Modifier.width(100.dp)
                )
            }
        }

        if (project.subtasks.isEmpty()) {
            item {
                EmptyState(
                    title = "No Subtasks",
                    subtitle = "Add subtasks to track detailed project progress."
                )
            }
        } else {
            items(project.subtasks, key = { it.id }) { subtask ->
                SubtaskCard(
                    subtask = subtask,
                    onToggleComplete = { viewModel.toggleSubtaskComplete(subtask) },
                    onEdit = { onEditSubtask(project.id, subtask.id) },
                    onDelete = { viewModel.deleteSubtask(subtask.id) }
                )
            }
        }
    }
}

@Composable
fun SubtaskCard(
    subtask: Subtask,
    onToggleComplete: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
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
                Checkbox(
                    checked = subtask.progress == 100,
                    onCheckedChange = { onToggleComplete() }
                )
                Column {
                    Text(
                        text = subtask.title,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    if (subtask.description.isNotBlank()) {
                        Text(
                            text = subtask.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Text(
                        text = "Progress: ${subtask.progress}%",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Row {
                TextButton(onClick = onEdit) {
                    Text("Edit")
                }
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Del")
                }
            }
        }
    }
}
