package com.hathway.projecttracker.domain.model

import kotlinx.serialization.Serializable

/**
 * Subtask domain model.
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
    val createdAt: Long = 0L,
    val hasActivity: Boolean = false,
    val activityDate: String? = null,
    val activityStartTime: String? = null,
    val activityEndTime: String? = null,
    val activityDuration: String? = null,
    val activityNotes: String? = null
)
