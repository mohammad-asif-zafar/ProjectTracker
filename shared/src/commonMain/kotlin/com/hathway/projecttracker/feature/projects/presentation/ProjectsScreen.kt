package com.hathway.projecttracker.feature.projects.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.FilterAlt
import androidx.compose.material.icons.rounded.Layers
import androidx.compose.material.icons.rounded.RadioButtonUnchecked
import androidx.compose.material.icons.rounded.Schedule
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hathway.projecttracker.core.ui.components.EmptyState
import com.hathway.projecttracker.core.ui.components.LoadingIndicator
import com.hathway.projecttracker.core.ui.components.ProjectIconView
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import com.hathway.projecttracker.core.ui.theme.SuccessGreen
import com.hathway.projecttracker.core.ui.theme.WarningOrange
import com.hathway.projecttracker.core.ui.theme.rememberBackgroundPainter
import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.model.Subtask

@Composable
fun ProjectsScreen(
    viewModel: ProjectsViewModel,
    onNavigateToProjectDetail: (String) -> Unit,
    onNavigateToAddProject: () -> Unit,
    modifier: Modifier = Modifier,
    onBack: () -> Unit, // <-- Add back callback
) {
    val state by viewModel.uiState.collectAsState()

    ProjectsContent(
        projects = state.projects,
        searchQuery = state.searchQuery,
        selectedFilter = state.selectedFilter,
        isLoading = state.isLoading,
        onSearchQueryChanged = viewModel::onSearchQueryChanged,
        onFilterSelected = viewModel::onFilterSelected,
        onNavigateToProjectDetail = onNavigateToProjectDetail,
        onNavigateToAddProject = onNavigateToAddProject,
        modifier = modifier,
        onBack = onBack, // <-- Pass down
    )
}

@Composable
fun ProjectsContent(
    projects: List<Project>,
    searchQuery: String,
    selectedFilter: ProjectStatus?,
    isLoading: Boolean,
    onSearchQueryChanged: (String) -> Unit,
    onFilterSelected: (ProjectStatus?) -> Unit,
    onNavigateToProjectDetail: (String) -> Unit,
    onNavigateToAddProject: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        // 1. Full screen background image
        Image(
            painter = rememberBackgroundPainter(),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // 2. Subtle scrim for top header text contrast
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.12f))
        )

        // 3. Main Padded Content Column (Header now sits inside here!)
        Column(
            modifier = Modifier.fillMaxSize().statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            // Header Row: Back Button + Title/Subtitle on left, Rounded Square '+' Button on right
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.size(40.dp)
                            .background(Color.White.copy(alpha = 0.2f), shape = CircleShape)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back to Dashboard",
                            tint = Color.White,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    Column {
                        Text(
                            text = "Projects", style = MaterialTheme.typography.headlineLarge.copy(
                                fontWeight = FontWeight.Bold, fontSize = 26.sp
                            ), color = Color.White
                        )
                        Text(
                            text = "Manage and track your projects",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White.copy(alpha = 0.85f)
                        )
                    }
                }

                Surface(
                    onClick = onNavigateToAddProject,
                    modifier = Modifier.size(46.dp),
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White.copy(alpha = 0.25f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.Add,
                            contentDescription = "Add Project",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Search Bar + Trailing Filter Icon Box
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchQueryChanged,
                    modifier = Modifier.weight(1f),
                    placeholder = {
                        Text(
                            text = "Search projects, keywords...",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { onSearchQueryChanged("") }) {
                                Icon(
                                    imageVector = Icons.Rounded.Close,
                                    contentDescription = "Clear search",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    },
                    shape = RoundedCornerShape(16.dp),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )

                Surface(
                    onClick = {
                        val nextFilter = when (selectedFilter) {
                            null -> ProjectStatus.IN_PROGRESS
                            ProjectStatus.IN_PROGRESS -> ProjectStatus.DONE
                            ProjectStatus.DONE -> ProjectStatus.PENDING
                            ProjectStatus.PENDING -> null
                        }
                        onFilterSelected(nextFilter)
                    },
                    modifier = Modifier.size(54.dp),
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    shadowElevation = 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Rounded.FilterAlt,
                            contentDescription = "Filter Projects",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Icon-Led Status Filter Pills & LazyColumn continue below...


            // 3. Icon-Led Status Filter Pills
            data class FilterItem(
                val status: ProjectStatus?,
                val label: String,
                val icon: ImageVector,
                val iconTint: Color
            )

            val filterItems = listOf(
                FilterItem(null, "All", Icons.Rounded.Layers, MaterialTheme.colorScheme.primary),
                FilterItem(
                    ProjectStatus.IN_PROGRESS,
                    "In Progress",
                    Icons.Rounded.RadioButtonUnchecked,
                    MaterialTheme.colorScheme.primary
                ),
                FilterItem(
                    ProjectStatus.DONE, "Done", Icons.Rounded.CheckCircleOutline, SuccessGreen
                ),
                FilterItem(ProjectStatus.PENDING, "Pending", Icons.Rounded.Schedule, WarningOrange)
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filterItems) { item ->
                    val isSelected = selectedFilter == item.status
                    Surface(
                        onClick = { onFilterSelected(item.status) },
                        shape = CircleShape,
                        color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null,
                                tint = if (isSelected) MaterialTheme.colorScheme.onPrimary else item.iconTint,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.bodySmall.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
// 4. Project Cards List
            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingIndicator()
                    }
                }

                projects.isEmpty() -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        EmptyState(
                            title = "No Projects Found",
                            subtitle = "Try adjusting your search query or filter."
                        )
                    }
                }

                else -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 32.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(projects, key = { it.id }) { project ->
                            DetailedProjectCard(
                                project = project,
                                onClick = { onNavigateToProjectDetail(project.id) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun DetailedProjectCard(
    project: Project, onClick: () -> Unit, modifier: Modifier = Modifier
) {
    val totalSubtasks = project.subtasks.size
    val completedSubtasks = project.subtasks.count { it.progress == 100 }

    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Top Row: Icon Box + Title/Subtitle + Status Badge Pill
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
                        modifier = Modifier.size(48.dp).clip(MaterialTheme.shapes.medium)
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        ProjectIconView(
                            iconStr = project.icon, modifier = Modifier.size(26.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = project.name,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold, fontSize = 17.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        val subtaskLabel =
                            if (totalSubtasks == 1) "1 Subtask" else "$totalSubtasks Subtasks"
                        val metaText = buildString {
                            if (project.shortName.isNotBlank()) {
                                append("${project.shortName}  •  ")
                            }
                            append(subtaskLabel)
                        }
                        Text(
                            text = metaText,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                ProjectStatusPill(status = project.status)
            }

            // Description Text Snippet
            if (project.description.isNotBlank()) {
                Text(
                    text = project.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Progress Label & Linear Bar
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Progress",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${project.progress}%",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = if (project.progress == 100) SuccessGreen else MaterialTheme.colorScheme.primary
                    )
                }

                val floatProg = (project.progress.coerceIn(0, 100)) / 100f
                LinearProgressIndicator(
                    progress = { floatProg },
                    modifier = Modifier.fillMaxWidth().height(8.dp).clip(CircleShape),
                    color = if (project.progress == 100) SuccessGreen else MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f)
                )
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.06f))

            // Footer Row: Total Subtasks vs Completed Count
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.List,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = if (totalSubtasks == 1) "1 Subtask" else "$totalSubtasks Subtasks",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CheckCircleOutline,
                        contentDescription = null,
                        tint = if (completedSubtasks > 0) SuccessGreen else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "$completedSubtasks Completed",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

@Composable
private fun ProjectStatusPill(status: ProjectStatus) {
    val (label, icon, tint, bgColor) = when (status) {
        ProjectStatus.DONE -> Quadruple(
            "Done", Icons.Rounded.CheckCircleOutline, SuccessGreen, SuccessGreen.copy(alpha = 0.14f)
        )

        ProjectStatus.IN_PROGRESS -> Quadruple(
            "In Progress",
            Icons.Rounded.RadioButtonUnchecked,
            Color(0xFFB26A00),
            WarningOrange.copy(alpha = 0.18f)
        )

        ProjectStatus.PENDING -> Quadruple(
            "Pending",
            Icons.Rounded.Schedule,
            MaterialTheme.colorScheme.primary,
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
        )
    }
    Surface(
        shape = CircleShape, color = bgColor
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = tint,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                color = tint
            )
        }
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

// --- Previews ---
private val mockProjectsList = listOf(
    Project(
        id = "1",
        name = "Little Sprout",
        shortName = "LS",
        keywords = listOf("android", "ios"),
        description = "Cross-platform mobile app for early childhood learning and daily habit tracking.",
        icon = "ai",
        progress = 55,
        status = ProjectStatus.IN_PROGRESS,
        subtasks = listOf(
            Subtask("1", "1", "Google Play Store", "", 55)
        )
    ), Project(
        id = "2",
        name = "STEP INTO",
        shortName = "St",
        keywords = listOf("kmp", "compose"),
        description = "Daily wellness and activity companion built with Compose Multiplatform.",
        icon = "code",
        progress = 0,
        status = ProjectStatus.PENDING,
        subtasks = emptyList()
    )
)

@Preview(name = "Projects Light Mode", showSystemUi = true)
@Composable
fun ProjectsScreenLightPreview() {
    ProTrackTheme(darkTheme = false) {
        ProjectsContent(
            projects = mockProjectsList,
            searchQuery = "",
            selectedFilter = null,
            isLoading = false,
            onSearchQueryChanged = {},
            onFilterSelected = {},
            onBack = {},
            onNavigateToProjectDetail = {},
            onNavigateToAddProject = {})
    }
}

@Preview(name = "Projects Dark Mode", showSystemUi = true)
@Composable
fun ProjectsScreenDarkPreview() {
    ProTrackTheme(darkTheme = true) {
        ProjectsContent(
            onBack = {},
            projects = mockProjectsList,
            searchQuery = "",
            selectedFilter = ProjectStatus.IN_PROGRESS,
            isLoading = false,
            onSearchQueryChanged = {},
            onFilterSelected = {},
            onNavigateToProjectDetail = {},
            onNavigateToAddProject = {})
    }
}