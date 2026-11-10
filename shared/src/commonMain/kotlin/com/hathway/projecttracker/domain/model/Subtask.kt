package com.hathway.projecttracker.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Subtask(
    val id: String,
    val projectId: String,
    val title: String,
    val description: String,
    val progress: Int, // 0 to 100
    val status: ProjectStatus = ProjectStatus.fromProgress(progress),
    val notes: String? = null
)
