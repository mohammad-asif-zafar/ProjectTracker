package com.hathway.projecttracker.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Project(
    val id: String,
    val name: String,
    val shortName: String,
    val keywords: List<String>,
    val description: String,
    val icon: String, // Icon identifier or name
    val progress: Int, // 0 to 100
    val status: ProjectStatus = ProjectStatus.fromProgress(progress),
    val subtasks: List<Subtask> = emptyList()
)
