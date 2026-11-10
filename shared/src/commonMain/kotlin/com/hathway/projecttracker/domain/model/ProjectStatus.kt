package com.hathway.projecttracker.domain.model

enum class ProjectStatus {
    PENDING,
    IN_PROGRESS,
    DONE;

    companion object {
        fun fromProgress(progress: Int): ProjectStatus {
            return when {
                progress <= 0 -> PENDING
                progress >= 100 -> DONE
                else -> IN_PROGRESS
            }
        }
    }
}
