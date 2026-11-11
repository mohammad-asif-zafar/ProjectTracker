package com.hathway.projecttracker.feature.projects.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Edit
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
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import com.hathway.projecttracker.feature.components.ProTrackButton
import org.jetbrains.compose.resources.painterResource
import projecttracker.shared.generated.resources.Res
import projecttracker.shared.generated.resources.home_bg

@Composable
fun AddEditProjectScreen(
    viewModel: AddEditProjectViewModel,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
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
        onSaveClick = viewModel::saveProject,
        onBack = onBack,
        modifier = modifier
    )
}

@Composable
fun AddEditProjectContent(
    uiState: AddEditProjectUiState,
    onNameChanged: (String) -> Unit,
    onShortNameChanged: (String) -> Unit,
    onKeywordsChanged: (String) -> Unit,
    onDescriptionChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
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

        // Top Header & Form Column scrolling together
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Top Header Banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color.White.copy(alpha = 0.2f), shape = CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = if (uiState.projectId == null) "Add Project" else "Edit Project",
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold, fontSize = 20.sp
                    ),
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Form Layout Container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Project Icon Card with Edit Pencil Badge
                Box(
                    modifier = Modifier.size(110.dp), contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier.size(96.dp).clip(MaterialTheme.shapes.large)
                            .background(MaterialTheme.colorScheme.surface),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = uiState.icon.ifBlank { "🌱" },
                            style = MaterialTheme.typography.headlineLarge.copy(fontSize = 44.sp)
                        )
                    }

                    // Pencil Edit Floating Button
                    IconButton(
                        onClick = { /* Pick icon */ },
                        modifier = Modifier.align(Alignment.BottomEnd).size(34.dp)
                            .background(MaterialTheme.colorScheme.surface, shape = CircleShape)
                            .padding(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Edit,
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
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
    }
}

// A curated collection of standard, highly recognizable multiplatform icons
private val AvailableIcons = listOf(
    Icons.Rounded.Folder,
    Icons.Rounded.Code,
    Icons.Rounded.LaptopMac,
    Icons.Rounded.Smartphone,
    Icons.Rounded.Brush,
    Icons.Rounded.Palette,
    Icons.Rounded.Build,
    Icons.Rounded.Settings,
    Icons.Rounded.Analytics,
    Icons.Rounded.PieChart,
    Icons.Rounded.Groups,
    Icons.Rounded.Assignment,
    Icons.Rounded.Cloud,
    Icons.Rounded.Lock,
    Icons.Rounded.Speed,
    Icons.Rounded.AutoAwesome
)


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
        icon = "🌱"
    )
    ProTrackTheme(darkTheme = false) {
        AddEditProjectContent(
            uiState = mockState,
            onNameChanged = {},
            onShortNameChanged = {},
            onKeywordsChanged = {},
            onDescriptionChanged = {},
            onSaveClick = {},
            onBack = {})
    }
}
