package com.hathway.projecttracker.domain.usecase.progress

import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectProgress
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.model.Subtask

class CalculateProjectProgressUseCase {
    fun calculateProgress(subtasks: List<Subtask>): Int {
        if (subtasks.isEmpty()) return 0
        val sum = subtasks.sumOf { it.progress }
        return (sum / subtasks.size).coerceIn(0, 100)
    }

    fun calculateProjectProgress(projects: List<Project>): ProjectProgress {
        val totalProjects = projects.size
        val completedProjects = projects.count { it.status == ProjectStatus.DONE }
        val inProgressProjects = projects.count { it.status == ProjectStatus.IN_PROGRESS }
        val pendingProjects = projects.count { it.status == ProjectStatus.PENDING }

        var totalSubtasks = 0
        var completedSubtasks = 0

        projects.forEach { project ->
            totalSubtasks += project.subtasks.size
            completedSubtasks += project.subtasks.count { it.progress == 100 }
        }

        val overallCompletion = if (projects.isEmpty()) {
            0
        } else {
            val totalProgressSum = projects.sumOf { it.progress }
            (totalProgressSum / totalProjects).coerceIn(0, 100)
        }

        return ProjectProgress(
            totalProjects = totalProjects,
            completedProjects = completedProjects,
            inProgressProjects = inProgressProjects,
            pendingProjects = pendingProjects,
            totalSubtasks = totalSubtasks,
            completedSubtasks = completedSubtasks,
            overallCompletion = overallCompletion
        )
    }
}
