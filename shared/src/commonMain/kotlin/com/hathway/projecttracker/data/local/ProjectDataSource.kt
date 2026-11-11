package com.hathway.projecttracker.data.local

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.hathway.projecttracker.core.util.currentTimeMillis
import com.hathway.projecttracker.data.db.ProjectDatabase
import com.hathway.projecttracker.domain.model.ActivityItem
import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.model.Subtask
import com.hathway.projecttracker.domain.usecase.progress.CalculateProjectProgressUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProjectDataSource(
    private val database: ProjectDatabase,
    private val progressCalculator: CalculateProjectProgressUseCase
) {
    private val projectQueries get() = database.projectEntityQueries
    private val subtaskQueries get() = database.subtaskEntityQueries
    private val activityQueries get() = database.activityEntityQueries

    val projectsFlow: Flow<List<Project>> = projectQueries.selectAll()
        .asFlow()
        .mapToList(Dispatchers.IO)
        .map { entities ->
            entities.map { mapEntityToProject(it) }
        }

    val activitiesFlow: Flow<List<ActivityItem>> = activityQueries.selectAllActivities()
        .asFlow()
        .mapToList(Dispatchers.IO)
        .map { entities ->
            entities.map {
                ActivityItem(
                    id = it.id,
                    title = it.title,
                    description = it.description,
                    type = it.type,
                    timestamp = it.timestamp
                )
            }
        }

    fun getProjects(): List<Project> {
        return projectQueries.selectAll().executeAsList().map { mapEntityToProject(it) }
    }

    fun getProjectById(id: String): Project? {
        val entity = projectQueries.selectById(id).executeAsOneOrNull() ?: return null
        return mapEntityToProject(entity)
    }

    fun getActivities(): List<ActivityItem> {
        return activityQueries.selectAllActivities().executeAsList().map {
            ActivityItem(
                id = it.id,
                title = it.title,
                description = it.description,
                type = it.type,
                timestamp = it.timestamp
            )
        }
    }

    fun addProject(project: Project) {
        val now = currentTimeMillis()
        val projectCreatedAt = if (project.createdAt == 0L) now else project.createdAt
        val calculatedProgress = progressCalculator.calculateProgress(project.subtasks)
        val newProject = project.copy(
            progress = calculatedProgress,
            status = ProjectStatus.fromProgress(calculatedProgress),
            createdAt = projectCreatedAt
        )
        database.transaction {
            projectQueries.insertProject(
                id = newProject.id,
                name = newProject.name,
                shortName = newProject.shortName,
                keywords = newProject.keywords.joinToString(","),
                description = newProject.description,
                icon = newProject.icon,
                progress = newProject.progress.toLong(),
                status = newProject.status.name,
                createdAt = newProject.createdAt
            )
            newProject.subtasks.forEach { subtask ->
                val subtaskCreatedAt = if (subtask.createdAt == 0L) now else subtask.createdAt
                subtaskQueries.insertSubtask(
                    id = subtask.id,
                    projectId = newProject.id,
                    title = subtask.title,
                    description = subtask.description,
                    progress = subtask.progress.toLong(),
                    status = subtask.status.name,
                    notes = subtask.notes,
                    icon = subtask.icon,
                    createdAt = subtaskCreatedAt
                )
            }
            activityQueries.insertActivity(
                id = "act-${kotlin.uuid.Uuid.random()}",
                title = "Project Created",
                description = "Created project '${newProject.name}'",
                type = "PROJECT",
                timestamp = now
            )
        }
    }

    fun updateProject(project: Project) {
        val now = currentTimeMillis()
        val calculatedProgress = progressCalculator.calculateProgress(project.subtasks)
        val updated = project.copy(
            progress = calculatedProgress,
            status = ProjectStatus.fromProgress(calculatedProgress)
        )
        database.transaction {
            projectQueries.updateProject(
                name = updated.name,
                shortName = updated.shortName,
                keywords = updated.keywords.joinToString(","),
                description = updated.description,
                icon = updated.icon,
                progress = updated.progress.toLong(),
                status = updated.status.name,
                createdAt = updated.createdAt.let { if (it == 0L) now else it },
                id = updated.id
            )
            val existingSubtasks = subtaskQueries.selectSubtasksForProject(updated.id).executeAsList()
            val existingSubtaskIds = existingSubtasks.map { it.id }.toSet()
            val newSubtaskIds = updated.subtasks.map { it.id }.toSet()

            existingSubtaskIds.subtract(newSubtaskIds).forEach { subtaskId ->
                subtaskQueries.deleteSubtask(subtaskId)
            }

            updated.subtasks.forEach { subtask ->
                val existingSubtask = existingSubtasks.find { it.id == subtask.id }
                val subtaskCreatedAt = existingSubtask?.createdAt ?: if (subtask.createdAt == 0L) now else subtask.createdAt
                subtaskQueries.insertSubtask(
                    id = subtask.id,
                    projectId = updated.id,
                    title = subtask.title,
                    description = subtask.description,
                    progress = subtask.progress.toLong(),
                    status = subtask.status.name,
                    notes = subtask.notes,
                    icon = subtask.icon,
                    createdAt = subtaskCreatedAt
                )
            }
            activityQueries.insertActivity(
                id = "act-${kotlin.uuid.Uuid.random()}",
                title = "Project Updated",
                description = "Updated project '${updated.name}'",
                type = "PROJECT",
                timestamp = now
            )
        }
    }

    fun deleteProject(id: String) {
        val projectEntity = projectQueries.selectById(id).executeAsOneOrNull()
        val projectName = projectEntity?.name ?: id
        database.transaction {
            subtaskQueries.deleteSubtasksForProject(id)
            projectQueries.deleteProject(id)
            activityQueries.insertActivity(
                id = "act-${kotlin.uuid.Uuid.random()}",
                title = "Project Deleted",
                description = "Deleted project '$projectName'",
                type = "PROJECT",
                timestamp = currentTimeMillis()
            )
        }
    }

    fun addSubtask(subtask: Subtask) {
        val projectEntity = projectQueries.selectById(subtask.projectId).executeAsOneOrNull() ?: return
        val currentProject = mapEntityToProject(projectEntity)
        val now = currentTimeMillis()
        val subtaskCreatedAt = if (subtask.createdAt == 0L) now else subtask.createdAt
        val subtaskToInsert = subtask.copy(createdAt = subtaskCreatedAt)
        val updatedSubtasks = currentProject.subtasks + subtaskToInsert
        val newProgress = progressCalculator.calculateProgress(updatedSubtasks)

        database.transaction {
            subtaskQueries.insertSubtask(
                id = subtaskToInsert.id,
                projectId = subtaskToInsert.projectId,
                title = subtaskToInsert.title,
                description = subtaskToInsert.description,
                progress = subtaskToInsert.progress.toLong(),
                status = subtaskToInsert.status.name,
                notes = subtaskToInsert.notes,
                icon = subtaskToInsert.icon,
                createdAt = subtaskToInsert.createdAt
            )
            projectQueries.updateProject(
                name = currentProject.name,
                shortName = currentProject.shortName,
                keywords = currentProject.keywords.joinToString(","),
                description = currentProject.description,
                icon = currentProject.icon,
                progress = newProgress.toLong(),
                status = ProjectStatus.fromProgress(newProgress).name,
                createdAt = currentProject.createdAt,
                id = currentProject.id
            )
            activityQueries.insertActivity(
                id = "act-${kotlin.uuid.Uuid.random()}",
                title = "Subtask Added",
                description = "Added '${subtaskToInsert.title}' to '${currentProject.name}'",
                type = "SUBTASK",
                timestamp = now
            )
        }
    }

    fun updateSubtask(subtask: Subtask) {
        val projectEntity = projectQueries.selectById(subtask.projectId).executeAsOneOrNull() ?: return
        val currentProject = mapEntityToProject(projectEntity)
        val existingSubtask = currentProject.subtasks.find { it.id == subtask.id }
        val subtaskCreatedAt = existingSubtask?.createdAt?.takeIf { it != 0L } ?: if (subtask.createdAt == 0L) currentTimeMillis() else subtask.createdAt
        val subtaskToUpdate = subtask.copy(createdAt = subtaskCreatedAt)
        val now = currentTimeMillis()

        val updatedSubtasks = currentProject.subtasks.map { if (it.id == subtask.id) subtaskToUpdate else it }
        val newProgress = progressCalculator.calculateProgress(updatedSubtasks)

        database.transaction {
            subtaskQueries.updateSubtask(
                projectId = subtaskToUpdate.projectId,
                title = subtaskToUpdate.title,
                description = subtaskToUpdate.description,
                progress = subtaskToUpdate.progress.toLong(),
                status = subtaskToUpdate.status.name,
                notes = subtaskToUpdate.notes,
                icon = subtaskToUpdate.icon,
                createdAt = subtaskToUpdate.createdAt,
                id = subtaskToUpdate.id
            )
            projectQueries.updateProject(
                name = currentProject.name,
                shortName = currentProject.shortName,
                keywords = currentProject.keywords.joinToString(","),
                description = currentProject.description,
                icon = currentProject.icon,
                progress = newProgress.toLong(),
                status = ProjectStatus.fromProgress(newProgress).name,
                createdAt = currentProject.createdAt,
                id = currentProject.id
            )
            activityQueries.insertActivity(
                id = "act-${kotlin.uuid.Uuid.random()}",
                title = "Subtask Updated",
                description = "Updated '${subtaskToUpdate.title}' (${subtaskToUpdate.progress}%)",
                type = "SUBTASK",
                timestamp = now
            )
        }
    }

    fun deleteSubtask(subtaskId: String) {
        val subtaskEntity = subtaskQueries.selectSubtaskById(subtaskId).executeAsOneOrNull() ?: return
        val projectId = subtaskEntity.projectId
        val projectEntity = projectQueries.selectById(projectId).executeAsOneOrNull() ?: return
        val currentProject = mapEntityToProject(projectEntity)
        val subtaskTitle = subtaskEntity.title
        val updatedSubtasks = currentProject.subtasks.filter { it.id != subtaskId }
        val newProgress = progressCalculator.calculateProgress(updatedSubtasks)
        val now = currentTimeMillis()

        database.transaction {
            subtaskQueries.deleteSubtask(subtaskId)
            projectQueries.updateProject(
                name = currentProject.name,
                shortName = currentProject.shortName,
                keywords = currentProject.keywords.joinToString(","),
                description = currentProject.description,
                icon = currentProject.icon,
                progress = newProgress.toLong(),
                status = ProjectStatus.fromProgress(newProgress).name,
                createdAt = currentProject.createdAt,
                id = currentProject.id
            )
            activityQueries.insertActivity(
                id = "act-${kotlin.uuid.Uuid.random()}",
                title = "Subtask Deleted",
                description = "Deleted '$subtaskTitle' from '${currentProject.name}'",
                type = "SUBTASK",
                timestamp = now
            )
        }
    }

    private fun mapEntityToProject(entity: com.hathway.projecttracker.data.db.ProjectEntity): Project {
        val subtaskEntities = subtaskQueries.selectSubtasksForProject(entity.id).executeAsList()
        val subtasks = subtaskEntities.map { subtaskEntity ->
            Subtask(
                id = subtaskEntity.id,
                projectId = subtaskEntity.projectId,
                title = subtaskEntity.title,
                description = subtaskEntity.description,
                progress = subtaskEntity.progress.toInt(),
                status = try {
                    ProjectStatus.valueOf(subtaskEntity.status)
                } catch (_: Exception) {
                    ProjectStatus.fromProgress(subtaskEntity.progress.toInt())
                },
                notes = subtaskEntity.notes,
                icon = subtaskEntity.icon,
                createdAt = subtaskEntity.createdAt
            )
        }
        val keywordsList = if (entity.keywords.isBlank()) emptyList() else entity.keywords.split(",").map { it.trim() }.filter { it.isNotEmpty() }
        val status = try {
            ProjectStatus.valueOf(entity.status)
        } catch (_: Exception) {
            ProjectStatus.fromProgress(entity.progress.toInt())
        }
        return Project(
            id = entity.id,
            name = entity.name,
            shortName = entity.shortName,
            keywords = keywordsList,
            description = entity.description,
            icon = entity.icon,
            progress = entity.progress.toInt(),
            status = status,
            subtasks = subtasks,
            createdAt = entity.createdAt
        )
    }
}
