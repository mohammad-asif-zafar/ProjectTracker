package com.hathway.projecttracker.feature.projects.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.usecase.project.DeleteProjectUseCase
import com.hathway.projecttracker.domain.usecase.project.GetProjectsUseCase
import com.hathway.projecttracker.domain.usecase.project.SearchProjectsUseCase
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ProjectsUiState(
    val isLoading: Boolean = true,
    val projects: List<Project> = emptyList(),
    val searchQuery: String = "",
    val selectedFilter: ProjectStatus? = null
)

class ProjectsViewModel(
    getProjectsUseCase: GetProjectsUseCase,
    private val searchProjectsUseCase: SearchProjectsUseCase,
    private val deleteProjectUseCase: DeleteProjectUseCase
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedFilter = MutableStateFlow<ProjectStatus?>(null)

    val uiState: StateFlow<ProjectsUiState> = combine(
        getProjectsUseCase(),
        _searchQuery,
        _selectedFilter
    ) { projects, query, filter ->
        var result = projects
        if (query.isNotBlank()) {
            val lower = query.lowercase()
            result = result.filter {
                it.name.lowercase().contains(lower) ||
                        it.shortName.lowercase().contains(lower) ||
                        it.description.lowercase().contains(lower) ||
                        it.keywords.any { kw -> kw.lowercase().contains(lower) }
            }
        }
        if (filter != null) {
            result = result.filter { it.status == filter }
        }
        ProjectsUiState(
            isLoading = false,
            projects = result,
            searchQuery = query,
            selectedFilter = filter
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ProjectsUiState()
    )

    fun onSearchQueryChanged(query: String) {
        _searchQuery.value = query
    }

    fun onFilterSelected(status: ProjectStatus?) {
        _selectedFilter.value = status
    }

    fun deleteProject(id: String) {
        viewModelScope.launch {
            deleteProjectUseCase(id)
        }
    }
}
