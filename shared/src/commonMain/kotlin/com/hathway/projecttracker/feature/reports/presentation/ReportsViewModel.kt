package com.hathway.projecttracker.feature.reports.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectProgress
import com.hathway.projecttracker.domain.usecase.progress.CalculateProjectProgressUseCase
import com.hathway.projecttracker.domain.usecase.project.GetProjectsUseCase
import kotlinx.coroutines.flow.*

data class ReportsUiState(
    val isLoading: Boolean = true,
    val projects: List<Project> = emptyList(),
    val progress: ProjectProgress = ProjectProgress(0, 0, 0, 0, 0, 0, 0)
)

class ReportsViewModel(
    getProjectsUseCase: GetProjectsUseCase,
    private val progressCalculator: CalculateProjectProgressUseCase
) : ViewModel() {

    val uiState: StateFlow<ReportsUiState> = getProjectsUseCase()
        .map { projects ->
            ReportsUiState(
                isLoading = false,
                projects = projects,
                progress = progressCalculator.calculateProjectProgress(projects)
            )
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ReportsUiState()
        )
}
