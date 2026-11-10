package com.hathway.projecttracker.feature.subtasks.presentation

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
fun AddEditSubtaskScreen(
    viewModel: AddEditSubtaskViewModel,
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
            text = if (state.subtaskId == null) "New Subtask" else "Edit Subtask",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        OutlinedTextField(
            value = state.title,
            onValueChange = { viewModel.onTitleChanged(it) },
            label = { Text("Subtask Title") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        OutlinedTextField(
            value = state.description,
            onValueChange = { viewModel.onDescriptionChanged(it) },
            label = { Text("Description") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Column {
            Text(
                text = "Progress: ${state.progress}%",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Slider(
                value = state.progress.toFloat(),
                onValueChange = { viewModel.onProgressChanged(it.toInt()) },
                valueRange = 0f..100f,
                steps = 19 // 5% increments
            )
        }

        OutlinedTextField(
            value = state.notes,
            onValueChange = { viewModel.onNotesChanged(it) },
            label = { Text("Optional Notes") },
            modifier = Modifier.fillMaxWidth(),
            minLines = 2
        )

        Spacer(modifier = Modifier.height(16.dp))

        PrimaryButton(
            text = if (state.subtaskId == null) "Create Subtask" else "Save Subtask",
            onClick = { viewModel.saveSubtask() },
            enabled = !state.isSaving && state.title.isNotBlank()
        )
    }
}
