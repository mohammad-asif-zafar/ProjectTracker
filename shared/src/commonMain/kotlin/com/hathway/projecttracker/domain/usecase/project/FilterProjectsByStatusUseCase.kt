package com.hathway.projecttracker.domain.usecase.project

import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.repository.ProjectRepository

class FilterProjectsByStatusUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke(status: ProjectStatus?): List<Project> = repository.filterProjectsByStatus(status)
}
