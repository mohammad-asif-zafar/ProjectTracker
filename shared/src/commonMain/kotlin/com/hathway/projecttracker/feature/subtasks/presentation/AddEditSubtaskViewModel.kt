package com.hathway.projecttracker.feature.subtasks.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.model.Subtask
import com.hathway.projecttracker.domain.repository.SubtaskRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEditSubtaskUiState(
    val projectId: String = "",
    val subtaskId: String? = null,
    val title: String = "",
    val description: String = "",
    val progress: Int = 0,
    val notes: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

class AddEditSubtaskViewModel(
    private val subtaskRepository: SubtaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditSubtaskUiState())
    val uiState: StateFlow<AddEditSubtaskUiState> = _uiState.asStateFlow()

    fun initSubtask(projectId: String, subtaskId: String?) {
        if (_uiState.value.projectId.isBlank()) {
            _uiState.update { it.copy(projectId = projectId, subtaskId = subtaskId) }
            if (subtaskId != null) {
                viewModelScope.launch {
                    subtaskRepository.getSubtaskById(subtaskId)?.let { subtask ->
                        _uiState.update {
                            it.copy(
                                title = subtask.title,
                                description = subtask.description,
                                progress = subtask.progress,
                                notes = subtask.notes ?: ""
                            )
                        }
                    }
                }
            }
        }
    }

    fun onTitleChanged(title: String) {
        _uiState.update { it.copy(title = title) }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onProgressChanged(progress: Int) {
        _uiState.update { it.copy(progress = progress.coerceIn(0, 100)) }
    }

    fun onNotesChanged(notes: String) {
        _uiState.update { it.copy(notes = notes) }
    }

    fun saveSubtask() {
        val current = _uiState.value
        if (current.title.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val status = ProjectStatus.fromProgress(current.progress)

            if (current.subtaskId == null) {
                val newSubtask = Subtask(
                    id = "sub-${(1000..9999).random()}",
                    projectId = current.projectId,
                    title = current.title,
                    description = current.description,
                    progress = current.progress,
                    status = status,
                    notes = current.notes.ifBlank { null }
                )
                subtaskRepository.addSubtask(newSubtask)
            } else {
                val existing = subtaskRepository.getSubtaskById(current.subtaskId)
                if (existing != null) {
                    val updated = existing.copy(
                        title = current.title,
                        description = current.description,
                        progress = current.progress,
                        status = status,
                        notes = current.notes.ifBlank { null }
                    )
                    subtaskRepository.updateSubtask(updated)
                }
            }
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
