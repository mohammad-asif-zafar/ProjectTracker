package com.hathway.projecttracker.domain.model

data class ProjectProgress(
    val totalProjects: Int,
    val completedProjects: Int,
    val inProgressProjects: Int,
    val pendingProjects: Int,
    val totalSubtasks: Int,
    val completedSubtasks: Int,
    val overallCompletion: Int
)
