package com.hathway.projecttracker.domain.usecase.project

import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.repository.ProjectRepository

class UpdateProjectUseCase(private val repository: ProjectRepository) {
    suspend operator fun invoke(project: Project) {
        repository.updateProject(project)
    }
}
