package com.hathway.projecttracker.domain.usecase.project

import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.repository.ProjectRepository

class SearchProjectsUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke(query: String): List<Project> = repository.searchProjects(query)
}
