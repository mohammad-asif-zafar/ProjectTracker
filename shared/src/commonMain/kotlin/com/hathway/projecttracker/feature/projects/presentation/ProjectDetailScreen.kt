package com.hathway.projecttracker.feature.projects.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.HourglassEmpty
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
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
import com.hathway.projecttracker.core.ui.components.EmptyState
import com.hathway.projecttracker.core.ui.components.LoadingIndicator
import com.hathway.projecttracker.core.ui.components.PrimaryButton
import com.hathway.projecttracker.core.ui.components.ProjectIconView
import com.hathway.projecttracker.core.ui.components.TaskBrandIcon
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import com.hathway.projecttracker.core.ui.theme.SuccessGreen
import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.model.Subtask
import com.hathway.projecttracker.feature.components.ProTrackButton
import com.hathway.projecttracker.feature.subtasks.presentation.UpdateSubtaskBottomSheet
import org.jetbrains.compose.resources.painterResource
import projecttracker.shared.generated.resources.Res
import projecttracker.shared.generated.resources.home_bg

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
        Box(
            modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(Res.drawable.home_bg),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            EmptyState(
                title = "Project Not Found",
                subtitle = "The project may have been deleted.",
                action = { PrimaryButton(text = "Go Back", onClick = onBack) })
        }
        return
    }

    ProjectDetailContent(
        project = project,
        onBack = onBack,
        onEditProject = onEditProject,
        onAddSubtask = onAddSubtask,
        onEditSubtask = onEditSubtask,
        onDeleteProject = { viewModel.deleteProject(onBack) },
        onUpdateSubtaskProgress = { subtaskId, prog, status ->
            viewModel.updateSubtaskProgress(subtaskId, prog, status)
        },
        modifier = modifier
    )
}

@Composable
fun ProjectDetailContent(
    project: Project,
    onBack: () -> Unit,
    onEditProject: (String) -> Unit,
    onAddSubtask: (String) -> Unit,
    onEditSubtask: (String, String) -> Unit,
    onDeleteProject: () -> Unit,
    onUpdateSubtaskProgress: (String, Int, ProjectStatus) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Subtasks, 1 = Overview
    var subtaskToUpdate by remember { mutableStateOf<Subtask?>(null) }

    Box(modifier = modifier.fillMaxSize()) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary))

        Column(modifier = Modifier.fillMaxSize()) {
            // 1. Transparent Top App Bar Layout
            Row(
                modifier = Modifier.fillMaxWidth().statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Project Details", style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold, fontSize = 20.sp
                        ), color = Color.White
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { onEditProject(project.id) }) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
                            contentDescription = "Edit Project",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    IconButton(onClick = onDeleteProject) {
                        Icon(
                            imageVector = Icons.Rounded.MoreVert,
                            contentDescription = "Options",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }
            }

            // 2. The Curved Base Panel Wrapping Elements Content
            Surface(
                modifier = Modifier.fillMaxWidth().weight(1f).padding(top = 8.dp),
                shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    // Profile Metadata Row
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(64.dp).clip(MaterialTheme.shapes.large)
                                    .background(MaterialTheme.colorScheme.primaryContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                ProjectIconView(
                                    iconStr = project.icon, modifier = Modifier.size(34.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = project.name,
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "${project.shortName} • ${project.subtasks.size} Tasks",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    // Linear Horizontal Progress Tracker
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            val floatProg = (project.progress.coerceIn(0, 100)) / 100f
                            LinearProgressIndicator(
                                progress = { floatProg },
                                modifier = Modifier.weight(1f).height(10.dp).clip(CircleShape),
                                color = SuccessGreen,
                                trackColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
                            )
                            Text(
                                text = "${project.progress}%",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }

                    // Quick Stat Grid Boxes
                    item {
                        val doneCount = project.subtasks.count { it.progress == 100 }
                        val inProgressCount = project.subtasks.count { it.progress in 1..99 }
                        val pendingCount = project.subtasks.count { it.progress == 0 }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            DetailStatBox(
                                icon = Icons.Rounded.CheckCircle,
                                iconColor = SuccessGreen,
                                count = doneCount,
                                label = "Done",
                                modifier = Modifier.weight(1f),
                                backgroundColor = SuccessGreen.copy(alpha = 0.08f)
                            )
                            DetailStatBox(
                                icon = Icons.Rounded.HourglassEmpty,
                                iconColor = MaterialTheme.colorScheme.primary,
                                count = inProgressCount,
                                label = "In Progress",
                                modifier = Modifier.weight(1f),
                                backgroundColor = MaterialTheme.colorScheme.primaryContainer.copy(
                                    alpha = 0.3f
                                )
                            )
                            DetailStatBox(
                                icon = Icons.Rounded.RadioButtonUnchecked,
                                iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                count = pendingCount,
                                label = "Pending",
                                modifier = Modifier.weight(1f),
                                backgroundColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.04f)
                            )
                        }
                    }

                    // Navigation Sub-Tabs Layout Group
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.fillMaxWidth()) {
                                TabHeaderItem(
                                    title = "Subtasks (${project.subtasks.size})",
                                    isSelected = selectedTab == 0,
                                    onClick = { selectedTab = 0 },
                                    modifier = Modifier.weight(1f)
                                )
                                TabHeaderItem(
                                    title = "Overview",
                                    isSelected = selectedTab == 1,
                                    onClick = { selectedTab = 1 },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                        }
                    }

                    if (selectedTab == 0) {
                        if (project.subtasks.isEmpty()) {
                            item {
                                EmptyState(
                                    title = "No Subtasks Found",
                                    subtitle = "Tap the button below to register your first workflow step."
                                )
                            }
                        } else {
                            itemsIndexed(
                                project.subtasks,
                                key = { _, subtask -> subtask.id }) { index, subtask ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    CompactSubtaskRow(
                                        subtask = subtask, onClick = {
                                            subtaskToUpdate = subtask
                                        })

                                    if (index < project.subtasks.lastIndex) {
                                        HorizontalDivider(
                                            modifier = Modifier.padding(start = 54.dp),
                                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f)
                                        )
                                    }
                                }
                            }
                        }

                        item {
                            Spacer(modifier = Modifier.height(12.dp))
                            ProTrackButton(
                                onClick = { onAddSubtask(project.id) },
                                text = "Add Subtask",
                                iconSymbol = "+",
                                modifier = Modifier.fillMaxWidth().height(48.dp)
                            )
                        }
                    } else {
                        item {
                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium,
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(
                                        alpha = 0.3f
                                    )
                                )
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    verticalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Text(
                                        text = "Project Analytics",
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Text(
                                        text = project.description.ifBlank { "No framework overview texts configured for this tracker item." },
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        if (subtaskToUpdate != null) {
            UpdateSubtaskBottomSheet(
                subtask = subtaskToUpdate!!,
                onDismissRequest = { subtaskToUpdate = null },
                onUpdateClick = { updatedProgress, updatedStatus ->
                    onUpdateSubtaskProgress(
                        subtaskToUpdate!!.id, updatedProgress, updatedStatus
                    )
                    subtaskToUpdate = null
                })
        }
    }
}

@Composable
private fun DetailStatBox(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    count: Int,
    label: String,
    backgroundColor: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier, shape = MaterialTheme.shapes.medium, color = backgroundColor
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "$count",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun TabHeaderItem(
    title: String, isSelected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.clickable(onClick = onClick).padding(vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier.fillMaxWidth(0.4f).height(3.dp)
                .background(if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent)
        )
    }
}

@Composable
private fun CompactSubtaskRow(
    subtask: Subtask, onClick: () -> Unit
) {
    val cleanKey = subtask.title.lowercase()
    val resolvedIconStr = when {
        "google play" in cleanKey || "play store" in cleanKey -> "googleplay"
        "app store" in cleanKey -> "appstore"
        "apple" in cleanKey || "ios" in cleanKey && "tablet" notIn cleanKey -> "apple"
        "tablet" in cleanKey -> "tablet"
        else -> "assignment"
    }
    Row(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick).padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                modifier = Modifier.size(40.dp).background(
                    MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f),
                    shape = MaterialTheme.shapes.small
                ), contentAlignment = Alignment.Center
            ) {
                TaskBrandIcon(iconKey = resolvedIconStr, modifier = Modifier.size(24.dp))
            }
            Text(
                text = subtask.title,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Text(
            text = "${subtask.progress}%",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = if (subtask.progress == 100) SuccessGreen else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private infix fun String.notIn(other: String): Boolean = !other.contains(this)

// --- Preview Configurations ---

private val mockProjectDetails = Project(
    id = "1",
    name = "Little Spout",
    shortName = "LS",
    keywords = listOf("android", "ios", "cross-platform"),
    description = "Cross-platform mobile client application deployment tracking workflow pipeline framework.",
    icon = "ai",
    progress = 25,
    status = ProjectStatus.IN_PROGRESS,
    subtasks = listOf(
        Subtask(
            id = "1",
            projectId = "1",
            title = "Google Play Store",
            description = "Production release track pipeline initialization",
            progress = 100,
            status = ProjectStatus.DONE,
            notes = null
        ), Subtask(
            id = "2",
            projectId = "1",
            title = "Android Tablet",
            description = "Adaptive layout compliance testing",
            progress = 0,
            status = ProjectStatus.PENDING,
            notes = null
        ), Subtask(
            id = "3",
            projectId = "1",
            title = "iOS Tablet",
            description = "Universal constraints configuration",
            progress = 0,
            status = ProjectStatus.PENDING,
            notes = null
        ), Subtask(
            id = "4",
            projectId = "1",
            title = "App Store",
            description = "App Store Connect store asset listing configuration",
            progress = 0,
            status = ProjectStatus.PENDING,
            notes = null
        )
    )
)

@Preview(name = "Light Details Layout", showSystemUi = true)
@Composable
fun ProjectDetailScreenLightPreview() {
    ProTrackTheme(darkTheme = false) {
        ProjectDetailContent(
            project = mockProjectDetails,
            onBack = {},
            onEditProject = {},
            onAddSubtask = {},
            onEditSubtask = { _, _ -> },
            onDeleteProject = {},
            onUpdateSubtaskProgress = { _, _, _ -> })
    }
}

@Preview(name = "Dark Details Layout", showSystemUi = true)
@Composable
fun ProjectDetailScreenDarkPreview() {
    ProTrackTheme(darkTheme = true) {
        ProjectDetailContent(
            project = mockProjectDetails,
            onBack = {},
            onEditProject = {},
            onAddSubtask = {},
            onEditSubtask = { _, _ -> },
            onDeleteProject = {},
            onUpdateSubtaskProgress = { _, _, _ -> })
    }
}
