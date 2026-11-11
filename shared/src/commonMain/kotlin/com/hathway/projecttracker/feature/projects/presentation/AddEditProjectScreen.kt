package com.hathway.projecttracker.feature.projects.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Edit
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
import com.hathway.projecttracker.core.ui.components.AvailableBrandIconKeys
import com.hathway.projecttracker.core.ui.components.AvailableMaterialIcons
import com.hathway.projecttracker.core.ui.components.ProjectIconView
import com.hathway.projecttracker.core.ui.components.iconVectorToString
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import com.hathway.projecttracker.feature.components.ProTrackButton
import com.hathway.projecttracker.feature.components.ProTrackTopAppBar
import com.hathway.projecttracker.core.ui.theme.rememberBackgroundPainter

@Composable
fun AddEditProjectScreen(
    viewModel: AddEditProjectViewModel, onBack: () -> Unit, modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsState()

    LaunchedEffect(state.isSaved) {
        if (state.isSaved) {
            onBack()
        }
    }

    AddEditProjectContent(
        uiState = state,
        onNameChanged = viewModel::onNameChanged,
        onShortNameChanged = viewModel::onShortNameChanged,
        onKeywordsChanged = viewModel::onKeywordsChanged,
        onDescriptionChanged = viewModel::onDescriptionChanged,
        onIconChanged = viewModel::onIconChanged,
        onSaveClick = viewModel::saveProject,
        onBack = onBack,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProjectContent(
    uiState: AddEditProjectUiState,
    onNameChanged: (String) -> Unit,
    onShortNameChanged: (String) -> Unit,
    onKeywordsChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onIconChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showIconPicker by remember { mutableStateOf(false) }

    Box(
        modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = rememberBackgroundPainter(),
            contentDescription = "Background",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        // Subtle dark tint overlay for readability
        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.15f))
        )

        // Top Header & Form Column scrolling together
        Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState())
        ) {
            // Reusable Top App Bar floating transparently over full screen asset
            ProTrackTopAppBar(
                title = if (uiState.projectId == null) "Add Project" else "Edit Project",
                onBackClick = onBack
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Form Layout Container
            Column(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Project Icon Card with Edit Pencil Badge
                Box(
                    modifier = Modifier.size(110.dp).clickable { showIconPicker = true },
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.size(96.dp).clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        ProjectIconView(
                            iconStr = uiState.icon, modifier = Modifier.size(44.dp)
                        )
                    }

                    // Pencil Edit Floating Button
                    IconButton(
                        onClick = { showIconPicker = true },
                        modifier = Modifier.align(Alignment.BottomEnd).size(34.dp)
                            .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Icon",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Input Form Block inside a surface card for gorgeous contrast over home_bg
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Project Name Input Field
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            RequiredLabel(text = "Project Name")
                            OutlinedTextField(
                                value = uiState.name,
                                onValueChange = onNameChanged,
                                placeholder = { Text("e.g. Little Spout") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium,
                                singleLine = true
                            )
                        }

                        // Short Name Input Field
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            RequiredLabel(text = "Short Name")
                            OutlinedTextField(
                                value = uiState.shortName,
                                onValueChange = onShortNameChanged,
                                placeholder = { Text("e.g. LS") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium,
                                singleLine = true
                            )
                        }

                        // Keywords Input Field
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Keywords",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            OutlinedTextField(
                                value = uiState.keywordsString,
                                onValueChange = onKeywordsChanged,
                                placeholder = { Text("android, ios, app, development") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium,
                                singleLine = true
                            )
                        }

                        // Description Input Field
                        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "Description",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            OutlinedTextField(
                                value = uiState.description,
                                onValueChange = onDescriptionChanged,
                                placeholder = { Text("Enter project breakdown details...") },
                                modifier = Modifier.fillMaxWidth(),
                                shape = MaterialTheme.shapes.medium,
                                minLines = 4
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Action Save Control
                ProTrackButton(
                    onClick = onSaveClick,
                    text = "Save",
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
// Icon Picker Bottom Sheet with Material Design Icons & Brand Icons
        if (showIconPicker) {
            ModalBottomSheet(
                onDismissRequest = { showIconPicker = false }) {
                Column(
                    modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Choose Project Icon",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    Text(
                        text = "Brand & Store Icons",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    AvailableBrandIconKeys.chunked(4).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start)
                        ) {
                            rowItems.forEach { iconKey ->
                                val isSelected = uiState.icon.lowercase() == iconKey
                                Surface(
                                    modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp))
                                        .clickable {
                                            onIconChanged(iconKey)
                                            showIconPicker = false
                                        },
                                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(
                                        alpha = 0.5f
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        ProjectIconView(
                                            iconStr = iconKey,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Material Icons",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    AvailableMaterialIcons.chunked(4).forEach { rowItems ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.Start)
                        ) {
                            rowItems.forEach { iconVector ->
                                val iconKey = iconVectorToString(iconVector)
                                val isSelected = uiState.icon.lowercase() == iconKey
                                Surface(
                                    modifier = Modifier.size(52.dp).clip(RoundedCornerShape(14.dp))
                                        .clickable {
                                            onIconChanged(iconKey)
                                            showIconPicker = false
                                        },
                                    color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant.copy(
                                        alpha = 0.5f
                                    ),
                                    shape = RoundedCornerShape(14.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = iconVector,
                                            contentDescription = null,
                                            tint = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                            modifier = Modifier.size(26.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(32.dp))
                }
            }
        }
    }
}
/**
 * Renders labels containing standard red asterisks matching screen layout requirements
 */
@Composable
private fun RequiredLabel(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = "*",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.error
        )
    }
}

// --- Previews & State Objects ---

@Preview(name = "Add Screen Match Light", showSystemUi = true)
@Composable
fun AddEditProjectScreenLightPreview() {
    val mockState = AddEditProjectUiState(
        name = "Little Spout",
        shortName = "LS",
        keywordsString = "android, ios, app, development",
        description = "A cross-platform app for learning and development.",
        icon = "code"
    )
    ProTrackTheme(darkTheme = false) {
        AddEditProjectContent(
            uiState = mockState,
            onNameChanged = {},
            onShortNameChanged = {},
            onKeywordsChanged = {},
            onDescriptionChanged = {},
            onIconChanged = {},
            onSaveClick = {},
            onBack = {})
    }
}
