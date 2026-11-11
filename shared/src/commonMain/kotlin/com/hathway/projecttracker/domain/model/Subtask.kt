package com.hathway.projecttracker.domain.model

import kotlinx.serialization.Serializable

/**
 * Subtask domain model.
 * Note: Subtask parent project ([projectId]) reassignment is not part of the current application scope;
 * subtasks are permanently scoped to their parent project.
 */
@Serializable
data class Subtask(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val progress: Int, // 0 to 100
    val status: ProjectStatus = ProjectStatus.fromProgress(progress),
    val notes: String? = null,
    val icon: String = "assignment",
    val createdAt: Long = 0L
)
