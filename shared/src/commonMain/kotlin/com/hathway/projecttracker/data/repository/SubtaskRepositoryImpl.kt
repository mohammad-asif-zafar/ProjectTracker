package com.hathway.projecttracker.data.repository

import com.hathway.projecttracker.data.local.ProjectDataSource
import com.hathway.projecttracker.domain.model.Subtask
import com.hathway.projecttracker.domain.repository.SubtaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SubtaskRepositoryImpl(private val dataSource: ProjectDataSource) : SubtaskRepository {
    override fun observeSubtasks(projectId: String): Flow<List<Subtask>> {
        return dataSource.projectsFlow.map { projects ->
            projects.find { it.id == projectId }?.subtasks ?: emptyList()
        }
    }

    override suspend fun getSubtasks(projectId: String): List<Subtask> {
        return dataSource.getProjectById(projectId)?.subtasks ?: emptyList()
    }

    override suspend fun getSubtaskById(id: String): Subtask? {
        dataSource.getProjects().forEach { project ->
            project.subtasks.find { it.id == id }?.let { return it }
        }
        return null
    }

    override suspend fun addSubtask(subtask: Subtask) {
        dataSource.addSubtask(subtask)
    }

    override suspend fun updateSubtask(subtask: Subtask) {
        dataSource.updateSubtask(subtask)
    }

    override suspend fun deleteSubtask(id: String) {
        dataSource.deleteSubtask(id)
    }
}
