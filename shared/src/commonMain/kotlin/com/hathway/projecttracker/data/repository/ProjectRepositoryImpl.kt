package com.hathway.projecttracker.data.repository

import com.hathway.projecttracker.data.local.ProjectDataSource
import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.repository.ProjectRepository
import kotlinx.coroutines.flow.Flow

class ProjectRepositoryImpl(private val dataSource: ProjectDataSource) : ProjectRepository {
    override fun observeProjects(): Flow<List<Project>> = dataSource.projectsFlow

    override suspend fun getProjects(): List<Project> = dataSource.getProjects()

    override suspend fun getProjectById(id: String): Project? = dataSource.getProjectById(id)

    override suspend fun addProject(project: Project) {
        dataSource.addProject(project)
    }

    override suspend fun updateProject(project: Project) {
        dataSource.updateProject(project)
    }

    override suspend fun deleteProject(id: String) {
        dataSource.deleteProject(id)
    }

    override suspend fun searchProjects(query: String): List<Project> {
        val all = dataSource.getProjects()
        if (query.isBlank()) return all
        val lowercaseQuery = query.lowercase()
        return all.filter {
            it.name.lowercase().contains(lowercaseQuery) ||
                    it.shortName.lowercase().contains(lowercaseQuery) ||
                    it.description.lowercase().contains(lowercaseQuery) ||
                    it.keywords.any { kw -> kw.lowercase().contains(lowercaseQuery) }
        }
    }

    override suspend fun filterProjectsByStatus(status: ProjectStatus?): List<Project> {
        val all = dataSource.getProjects()
        if (status == null) return all
        return all.filter { it.status == status }
    }
}
