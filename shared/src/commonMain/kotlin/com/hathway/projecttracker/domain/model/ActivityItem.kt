package com.hathway.projecttracker.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class ActivityItem(
    val id: String,
    val title: String,
    val description: String,
    val type: String, // "PROJECT" or "SUBTASK"
    val timestamp: Long
)
