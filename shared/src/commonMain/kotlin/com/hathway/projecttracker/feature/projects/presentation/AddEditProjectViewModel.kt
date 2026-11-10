package com.hathway.projecttracker.feature.projects.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.usecase.project.AddProjectUseCase
import com.hathway.projecttracker.domain.usecase.project.GetProjectByIdUseCase
import com.hathway.projecttracker.domain.usecase.project.UpdateProjectUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddEditProjectUiState(
    val projectId: String? = null,
    val name: String = "",
    val shortName: String = "",
    val keywordsString: String = "",
    val description: String = "",
    val icon: String = "📁",
    val isSaving: Boolean = false,
    val isSaved: Boolean = false
)

class AddEditProjectViewModel(
    private val getProjectByIdUseCase: GetProjectByIdUseCase,
    private val addProjectUseCase: AddProjectUseCase,
    private val updateProjectUseCase: UpdateProjectUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AddEditProjectUiState())
    val uiState: StateFlow<AddEditProjectUiState> = _uiState.asStateFlow()

    fun loadProject(projectId: String?) {
        if (projectId != null && _uiState.value.projectId == null) {
            _uiState.update { it.copy(projectId = projectId) }
            viewModelScope.launch {
                getProjectByIdUseCase(projectId)?.let { project ->
                    _uiState.update {
                        it.copy(
                            name = project.name,
                            shortName = project.shortName,
                            keywordsString = project.keywords.joinToString(", "),
                            description = project.description,
                            icon = project.icon
                        )
                    }
                }
            }
        }
    }

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onShortNameChanged(shortName: String) {
        _uiState.update { it.copy(shortName = shortName) }
    }

    fun onKeywordsChanged(keywords: String) {
        _uiState.update { it.copy(keywordsString = keywords) }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onIconChanged(icon: String) {
        _uiState.update { it.copy(icon = icon) }
    }

    fun saveProject() {
        val current = _uiState.value
        if (current.name.isBlank()) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val keywordsList = current.keywordsString.split(",").map { it.trim() }.filter { it.isNotBlank() }

            if (current.projectId == null) {
                val newProject = Project(
                    id = "proj-${(1000..9999).random()}",
                    name = current.name,
                    shortName = current.shortName.ifBlank { current.name },
                    keywords = keywordsList,
                    description = current.description,
                    icon = current.icon.ifBlank { "📁" },
                    progress = 0,
                    status = ProjectStatus.PENDING,
                    subtasks = emptyList()
                )
                addProjectUseCase(newProject)
            } else {
                val existing = getProjectByIdUseCase(current.projectId!!)
                if (existing != null) {
                    val updated = existing.copy(
                        name = current.name,
                        shortName = current.shortName.ifBlank { current.name },
                        keywords = keywordsList,
                        description = current.description,
                        icon = current.icon.ifBlank { "📁" }
                    )
                    updateProjectUseCase(updated)
                }
            }
            _uiState.update { it.copy(isSaving = false, isSaved = true) }
        }
    }
}
