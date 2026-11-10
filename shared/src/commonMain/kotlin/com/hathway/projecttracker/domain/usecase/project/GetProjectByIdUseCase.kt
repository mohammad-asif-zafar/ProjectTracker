package com.hathway.projecttracker.domain.usecase.project

import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.repository.ProjectRepository

class GetProjectByIdUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke(id: String): Project? = repository.getProjectById(id)
}
