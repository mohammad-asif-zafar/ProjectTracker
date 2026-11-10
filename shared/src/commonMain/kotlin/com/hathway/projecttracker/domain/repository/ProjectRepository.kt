package com.hathway.projecttracker.domain.repository

import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectStatus
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun observeProjects(): Flow<List<Project>>
    suspend fun getProjects(): List<Project>
    suspend fun getProjectById(id: String): Project?
    suspend fun addProject(project: Project)
    suspend fun updateProject(project: Project)
    suspend fun deleteProject(id: String)
    suspend fun searchProjects(query: String): List<Project>
    suspend fun filterProjectsByStatus(status: ProjectStatus?): List<Project>
}
