package com.hathway.projecttracker.feature.projects.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.Subtask
import com.hathway.projecttracker.domain.usecase.project.DeleteProjectUseCase
import com.hathway.projecttracker.domain.usecase.project.GetProjectsUseCase
import com.hathway.projecttracker.domain.usecase.subtask.DeleteSubtaskUseCase
import com.hathway.projecttracker.domain.usecase.subtask.UpdateSubtaskUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProjectDetailUiState(
    val isLoading: Boolean = true,
    val projectId: String = "",
    val project: Project? = null
)

class ProjectDetailViewModel(
    getProjectsUseCase: GetProjectsUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase,
    private val deleteSubtaskUseCase: DeleteSubtaskUseCase,
    private val updateSubtaskUseCase: UpdateSubtaskUseCase
) : ViewModel() {

    private val _projectIdFlow = MutableStateFlow("")

    val uiState: StateFlow<ProjectDetailUiState> = combine(
        getProjectsUseCase(),
        _projectIdFlow
    ) { projects, id ->
        val project = projects.find { it.id == id }
        ProjectDetailUiState(
            isLoading = false,
            projectId = id,
            project = project
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProjectDetailUiState()
    )

    fun setProjectId(id: String) {
        println("ProTrackLog: ProjectDetailViewModel setProjectId -> $id")
        _projectIdFlow.value = id
    }

    fun deleteProject(onSuccess: () -> Unit) {
        val id = _projectIdFlow.value
        println("ProTrackLog: ProjectDetailViewModel deleteProject clicked for id=$id")
        if (id.isBlank()) return
        viewModelScope.launch {
            deleteProjectUseCase(id)
            onSuccess()
        }
    }

    fun deleteSubtask(subtaskId: String) {
        println("ProTrackLog: ProjectDetailViewModel deleteSubtask clicked for id=$subtaskId")
        viewModelScope.launch {
            deleteSubtaskUseCase(subtaskId)
        }
    }

    fun toggleSubtaskComplete(subtask: Subtask) {
        println("ProTrackLog: ProjectDetailViewModel toggleSubtaskComplete clicked for subtaskId=${subtask.id}")
        viewModelScope.launch {
            val newProgress = if (subtask.progress == 100) 0 else 100
            val newStatus = com.hathway.projecttracker.domain.model.ProjectStatus.fromProgress(newProgress)
            val updated = subtask.copy(progress = newProgress, status = newStatus)
            updateSubtaskUseCase(updated)
        }
    }

    fun updateSubtaskProgress(subtaskId: String, progress: Int, status: com.hathway.projecttracker.domain.model.ProjectStatus) {
        println("ProTrackLog: ProjectDetailViewModel updateSubtaskProgress clicked for subtaskId=$subtaskId, progress=$progress, status=$status")
        viewModelScope.launch {
            val project = uiState.value.project ?: return@launch
            val subtask = project.subtasks.find { it.id == subtaskId } ?: return@launch
            val updated = subtask.copy(progress = progress, status = status)
            updateSubtaskUseCase(updated)
        }
    }
}
