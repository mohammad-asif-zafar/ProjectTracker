package com.hathway.projecttracker.di

import com.hathway.projecttracker.data.local.ProjectDataSource
import com.hathway.projecttracker.data.repository.ProjectRepositoryImpl
import com.hathway.projecttracker.data.repository.SubtaskRepositoryImpl
import com.hathway.projecttracker.domain.repository.ProjectRepository
import com.hathway.projecttracker.domain.repository.SubtaskRepository
import com.hathway.projecttracker.domain.usecase.progress.CalculateProjectProgressUseCase
import com.hathway.projecttracker.domain.usecase.project.*
import com.hathway.projecttracker.domain.usecase.subtask.*
import com.hathway.projecttracker.feature.dashboard.presentation.DashboardViewModel
import com.hathway.projecttracker.feature.projects.presentation.AddEditProjectViewModel
import com.hathway.projecttracker.feature.projects.presentation.ProjectDetailViewModel
import com.hathway.projecttracker.feature.projects.presentation.ProjectsViewModel
import com.hathway.projecttracker.feature.reports.presentation.ReportsViewModel
import com.hathway.projecttracker.feature.settings.presentation.SettingsViewModel
import com.hathway.projecttracker.feature.subtasks.presentation.AddEditSubtaskViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    // Local data source (singleton)
    single { ProjectDataSource() }

    // Repositories
    single<ProjectRepository> { ProjectRepositoryImpl(get()) }
    single<SubtaskRepository> { SubtaskRepositoryImpl(get()) }

    // Use Cases
    factory { CalculateProjectProgressUseCase() }
    factory { GetProjectsUseCase(get()) }
    factory { GetProjectByIdUseCase(get()) }
    factory { AddProjectUseCase(get()) }
    factory { UpdateProjectUseCase(get()) }
    factory { DeleteProjectUseCase(get()) }
    factory { SearchProjectsUseCase(get()) }
    factory { FilterProjectsByStatusUseCase(get()) }
    factory { GetSubtasksForProjectUseCase(get()) }
    factory { AddSubtaskUseCase(get()) }
    factory { UpdateSubtaskUseCase(get()) }
    factory { DeleteSubtaskUseCase(get()) }

    // ViewModels
    viewModel { DashboardViewModel(get(), get()) }
    viewModel { ProjectsViewModel(get(), get(), get()) }
    viewModel { AddEditProjectViewModel(get(), get(), get()) }
    viewModel { ProjectDetailViewModel(get(), get(), get(), get()) }
    viewModel { AddEditSubtaskViewModel(get()) }
    viewModel { ReportsViewModel(get(), get()) }
    viewModel { SettingsViewModel() }
}
