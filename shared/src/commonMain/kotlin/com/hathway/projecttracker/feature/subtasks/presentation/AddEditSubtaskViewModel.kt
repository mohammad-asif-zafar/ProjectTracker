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
    val icon: String = "assignment",
    val hasActivity: Boolean = false,
    val activityTitle: String = "Gym Activity Schedule",
    val activityDate: String = "13 November 2026",
    val activityStartTime: String = "6:00 PM",
    val activityEndTime: String = "7:30 PM",
    val activityDuration: String = "1 hr 30 min",
    val activityNotes: String = "",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

class AddEditSubtaskViewModel(
    private val subtaskRepository: SubtaskRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditSubtaskUiState())
    val uiState: StateFlow<AddEditSubtaskUiState> = _uiState.asStateFlow()

    fun initSubtask(projectId: String, subtaskId: String?) {
        if (_uiState.value.projectId != projectId || _uiState.value.subtaskId != subtaskId || _uiState.value.isSaved || (_uiState.value.projectId.isBlank() && _uiState.value.title.isBlank())) {
            _uiState.value = AddEditSubtaskUiState(projectId = projectId, subtaskId = subtaskId, isSaved = false)
            if (subtaskId != null) {
                viewModelScope.launch {
                    subtaskRepository.getSubtaskById(subtaskId)?.let { subtask ->
                        _uiState.update {
                            it.copy(
                                title = subtask.title,
                                description = subtask.description,
                                progress = subtask.progress,
                                notes = subtask.notes ?: "",
                                icon = subtask.icon.ifBlank { "assignment" },
                                hasActivity = subtask.hasActivity,
                                activityDate = subtask.activityDate ?: "13 November 2026",
                                activityStartTime = subtask.activityStartTime ?: "6:00 PM",
                                activityEndTime = subtask.activityEndTime ?: "7:30 PM",
                                activityDuration = subtask.activityDuration ?: "1 hr 30 min",
                                activityNotes = subtask.activityNotes ?: ""
                            )
                        }
                    }
                }
            }
        } else {
            _uiState.update { it.copy(isSaved = false) }
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

    fun onIconChanged(icon: String) {
        _uiState.update { it.copy(icon = icon) }
    }

    fun onToggleActivity(enabled: Boolean) {
        _uiState.update { it.copy(hasActivity = enabled) }
    }

    fun onActivityDateChanged(date: String) {
        _uiState.update { it.copy(activityDate = date) }
    }

    fun onActivityStartTimeChanged(time: String) {
        _uiState.update { it.copy(activityStartTime = time) }
    }

    fun onActivityEndTimeChanged(time: String) {
        _uiState.update { it.copy(activityEndTime = time) }
    }

    fun onActivityNotesChanged(notes: String) {
        _uiState.update { it.copy(activityNotes = notes) }
    }

    fun saveSubtask() {
        val current = _uiState.value
        if (current.title.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val status = ProjectStatus.fromProgress(current.progress)

            if (current.subtaskId == null) {
                val newSubtask = Subtask(
                    id = "sub-${kotlin.uuid.Uuid.random()}",
                    projectId = current.projectId,
                    title = current.title,
                    description = current.description,
                    progress = current.progress,
                    status = status,
                    notes = current.notes.ifBlank { null },
                    icon = current.icon.ifBlank { "assignment" },
                    hasActivity = current.hasActivity,
                    activityDate = current.activityDate.ifBlank { null },
                    activityStartTime = current.activityStartTime.ifBlank { null },
                    activityEndTime = current.activityEndTime.ifBlank { null },
                    activityDuration = current.activityDuration.ifBlank { null },
                    activityNotes = current.activityNotes.ifBlank { null }
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
                        notes = current.notes.ifBlank { null },
                        icon = current.icon.ifBlank { "assignment" },
                        hasActivity = current.hasActivity,
                        activityDate = current.activityDate.ifBlank { null },
                        activityStartTime = current.activityStartTime.ifBlank { null },
                        activityEndTime = current.activityEndTime.ifBlank { null },
                        activityDuration = current.activityDuration.ifBlank { null },
                        activityNotes = current.activityNotes.ifBlank { null }
                    )
                    subtaskRepository.updateSubtask(updated)
                }
            }
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
