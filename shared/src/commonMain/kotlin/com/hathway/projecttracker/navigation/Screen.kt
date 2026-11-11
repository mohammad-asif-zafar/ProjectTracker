package com.hathway.projecttracker.navigation

sealed class Screen(val route: String) {
    data object Dashboard : Screen("dashboard")
    data object Projects : Screen("projects")
    data object Reports : Screen("reports")
    data object Settings : Screen("settings")
    data object Notifications : Screen("notifications")
    data object AddEditProject : Screen("add_edit_project?projectId={projectId}") {
        fun createRoute(projectId: String? = null): String {
            return if (projectId != null) "add_edit_project?projectId=$projectId" else "add_edit_project"
        }
    }
    data object ProjectDetail : Screen("project_detail/{projectId}") {
        fun createRoute(projectId: String): String = "project_detail/$projectId"
    }
    data object AddEditSubtask : Screen("add_edit_subtask?projectId={projectId}&subtaskId={subtaskId}") {
        fun createRoute(projectId: String, subtaskId: String? = null): String {
            return if (subtaskId != null) {
                "add_edit_subtask?projectId=$projectId&subtaskId=$subtaskId"
            } else {
                "add_edit_subtask?projectId=$projectId"
            }
        }
    }
}
