package com.hathway.projecttracker.domain.usecase.project

import com.hathway.projecttracker.domain.repository.ProjectRepository

class DeleteProjectUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke(id: String) {
        repository.deleteProject(id)
    }
}
