package com.hathway.projecttracker.domain.usecase.project

import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow

class GetProjectsUseCase(private val repository: ProjectRepository) {
    operator fun invoke(): Flow<List<Project>> = repository.observeProjects()
}
