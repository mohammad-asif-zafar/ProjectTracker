package com.hathway.projecttracker.feature.projects.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.hathway.projecttracker.core.ui.components.PrimaryButton

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = if (state.projectId == null) "New Project" else "Edit Project",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        OutlinedTextField(
            value = state.name,
            onValueChange = { viewModel.onNameChanged(it) },
            label = { Text("Project Name") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.shortName,
            onValueChange = { viewModel.onShortNameChanged(it) },
            label = { Text("Short Name (e.g. ProTrack)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.keywordsString,
            onValueChange = { viewModel.onKeywordsChanged(it) },
            label = { Text("Keywords (comma separated)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.description,
            onValueChange = { viewModel.onDescriptionChanged(it) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 3
        )

        OutlinedTextField(
            value = state.icon,
            onValueChange = { viewModel.onIconChanged(it) },
            label = { Text("Project Icon Emoji (e.g. 🚀)") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = if (state.projectId == null) "Create Project" else "Save Changes",
            onClick = { viewModel.saveProject() },
            enabled = !state.isSaving && state.name.isNotBlank()
        )
    }
}
