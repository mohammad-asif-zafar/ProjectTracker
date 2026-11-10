package com.hathway.projecttracker.data.local

import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.model.Subtask
import com.hathway.projecttracker.domain.usecase.progress.CalculateProjectProgressUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ProjectDataSource {
    private val progressCalculator = CalculateProjectProgressUseCase()

    private val initialProjects = listOf(
        Project(
            id = "proj-1",
            name = "Little Spout",
            shortName = "LittleSpout",
            keywords = listOf("Mobile", "App", "Store"),
            description = "Cross-platform mobile application for community sprouting and eco tracking.",
            icon = "🌱",
            progress = 25,
            status = ProjectStatus.IN_PROGRESS,
            subtasks = listOf(
                Subtask("sub-1", "proj-1", "Google Play Store", "Deploy release bundle to Play Console", 100, ProjectStatus.DONE, "Published successfully"),
                Subtask("sub-2", "proj-1", "Android Tablet", "Adapt UI for large screen landscape & portrait", 0, ProjectStatus.PENDING, "Pending UI audit"),
                Subtask("sub-3", "proj-1", "iOS Tablet", "Ensure iPad split view support", 0, ProjectStatus.PENDING, "Scheduled for next sprint"),
                Subtask("sub-4", "proj-1", "App Store", "Prepare TestFlight beta build", 0, ProjectStatus.PENDING, "Awaiting provisioning profile")
            )
        ),
        Project(
            id = "proj-2",
            name = "Smart Home Hub",
            shortName = "HomeHub",
            keywords = listOf("IoT", "Automation"),
            description = "Centralized IoT control hub for smart home automation.",
            icon = "🏠",
            progress = 100,
            status = ProjectStatus.DONE,
            subtasks = listOf(
                Subtask("sub-5", "proj-2", "MQTT Protocol", "Implement broker connection and reconnection", 100, ProjectStatus.DONE),
                Subtask("sub-6", "proj-2", "UI Dashboard", "Material 3 control widgets", 100, ProjectStatus.DONE)
            )
        ),
        Project(
            id = "proj-3",
            name = "FinTech Ledger",
            shortName = "Ledger",
            keywords = listOf("Finance", "Security"),
            description = "Secure multi-currency wallet and transaction ledger.",
            icon = "💳",
            progress = 45,
            status = ProjectStatus.IN_PROGRESS,
            subtasks = listOf(
                Subtask("sub-7", "proj-3", "Biometric Auth", "BiometricPrompt integration", 90, ProjectStatus.IN_PROGRESS),
                Subtask("sub-8", "proj-3", "Encryption", "SQLCipher database encryption", 0, ProjectStatus.PENDING)
            )
        )
    )

    private val _projectsFlow = MutableStateFlow(initialProjects)
    val projectsFlow: StateFlow<List<Project>> = _projectsFlow.asStateFlow()

    fun getProjects(): List<Project> = _projectsFlow.value

    fun getProjectById(id: String): Project? = _projectsFlow.value.find { it.id == id }

    fun addProject(project: Project) {
        val calculatedProgress = progressCalculator.calculateProgress(project.subtasks)
        val newProject = project.copy(
            progress = calculatedProgress,
            status = ProjectStatus.fromProgress(calculatedProgress)
        )
        _projectsFlow.update { current -> current + newProject }
    }

    fun updateProject(project: Project) {
        val calculatedProgress = progressCalculator.calculateProgress(project.subtasks)
        val updated = project.copy(
            progress = calculatedProgress,
            status = ProjectStatus.fromProgress(calculatedProgress)
        )
        _projectsFlow.update { current ->
            current.map { if (it.id == updated.id) updated else it }
        }
    }

    fun deleteProject(id: String) {
        _projectsFlow.update { current -> current.filter { it.id != id } }
    }

    fun addSubtask(subtask: Subtask) {
        _projectsFlow.update { current ->
            current.map { project ->
                if (project.id == subtask.projectId) {
                    val updatedSubtasks = project.subtasks + subtask
                    val newProgress = progressCalculator.calculateProgress(updatedSubtasks)
                    project.copy(
                        subtasks = updatedSubtasks,
                        progress = newProgress,
                        status = ProjectStatus.fromProgress(newProgress)
                    )
                } else {
                    project
                }
            }
        }
    }

    fun updateSubtask(subtask: Subtask) {
        _projectsFlow.update { current ->
            current.map { project ->
                if (project.id == subtask.projectId) {
                    val updatedSubtasks = project.subtasks.map { if (it.id == subtask.id) subtask else it }
                    val newProgress = progressCalculator.calculateProgress(updatedSubtasks)
                    project.copy(
                        subtasks = updatedSubtasks,
                        progress = newProgress,
                        status = ProjectStatus.fromProgress(newProgress)
                    )
                } else {
                    project
                }
            }
        }
    }

    fun deleteSubtask(subtaskId: String) {
        _projectsFlow.update { current ->
            current.map { project ->
                if (project.subtasks.any { it.id == subtaskId }) {
                    val updatedSubtasks = project.subtasks.filter { it.id != subtaskId }
                    val newProgress = progressCalculator.calculateProgress(updatedSubtasks)
                    project.copy(
                        subtasks = updatedSubtasks,
                        progress = newProgress,
                        status = ProjectStatus.fromProgress(newProgress)
                    )
                } else {
                    project
                }
            }
        }
    }
}
