package com.hathway.projecttracker.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.hathway.projecttracker.core.ui.theme.ProTrackTheme
import com.hathway.projecttracker.feature.dashboard.presentation.DashboardScreen
import com.hathway.projecttracker.feature.dashboard.presentation.DashboardViewModel
import com.hathway.projecttracker.feature.projects.presentation.*
import com.hathway.projecttracker.feature.reports.presentation.ReportsScreen
import com.hathway.projecttracker.feature.reports.presentation.ReportsViewModel
import com.hathway.projecttracker.feature.settings.presentation.SettingsScreen
import com.hathway.projecttracker.feature.settings.presentation.SettingsViewModel
import com.hathway.projecttracker.feature.subtasks.presentation.AddEditSubtaskScreen
import com.hathway.projecttracker.feature.subtasks.presentation.AddEditSubtaskViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProTrackApp() {
    val settingsRepository =
        org.koin.compose.koinInject<com.hathway.projecttracker.data.repository.SettingsRepository>()
    val isDarkMode by settingsRepository.isDarkMode.collectAsState()

    ProTrackTheme(darkTheme = isDarkMode) {
        val navStack = remember { mutableStateListOf(Screen.Dashboard.route) }
        val currentRoute = navStack.lastOrNull() ?: Screen.Dashboard.route

        var currentProjectId by remember { mutableStateOf<String?>(null) }
        var currentSubtaskId by remember { mutableStateOf<String?>(null) }

        val bottomNavRoutes = listOf(
            Screen.Dashboard.route, Screen.Settings.route
        )

        // System Back Button handling
        ProTrackBackHandler(enabled = true) {
            if (navStack.size > 1) {
                navStack.removeAt(navStack.size - 1)
            } else if (currentRoute != Screen.Dashboard.route) {
                navStack.clear()
                navStack.add(Screen.Dashboard.route)
            }
        }

        fun navigateTo(route: String) {
            if (route in bottomNavRoutes) {
                // If switching to bottom nav tab, reset stack to that tab
                navStack.clear()
                navStack.add(route)
            } else {
                navStack.add(route)
            }
        }

        ResponsiveScaffold(
            currentRoute = currentRoute,
            onNavigate = { route -> navigateTo(route) }) { paddingValues ->
            val modifier = Modifier.fillMaxSize().padding(paddingValues)

            when {
                currentRoute == Screen.Dashboard.route -> {
                    val viewModel = koinViewModel<DashboardViewModel>()
                    DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToProjectDetail = { projectId ->
                            currentProjectId = projectId
                            navigateTo(Screen.ProjectDetail.createRoute(projectId))
                        },
                        onNavigateToAddProject = {
                            currentProjectId = null
                            navigateTo(Screen.AddEditProject.createRoute(null))
                        },
                        onNavigateToProjects = {
                            navigateTo(Screen.Projects.route)
                        },
                        modifier = modifier
                    )
                }

                currentRoute == Screen.Projects.route -> {
                    val viewModel = koinViewModel<ProjectsViewModel>()
                    ProjectsScreen(
                        viewModel = viewModel,
                        onNavigateToProjectDetail = { projectId ->
                            currentProjectId = projectId
                            navigateTo(Screen.ProjectDetail.createRoute(projectId))
                        },
                        onNavigateToAddProject = {
                            currentProjectId = null
                            navigateTo(Screen.AddEditProject.createRoute(null))
                        },
                        modifier = modifier,
                        onBack = { if (navStack.size > 1) navStack.removeAt(navStack.size - 1) })
                }

                currentRoute.startsWith("add_edit_project") -> {
                    val viewModel = koinViewModel<AddEditProjectViewModel>()
                    LaunchedEffect(currentProjectId) {
                        viewModel.loadProject(currentProjectId)
                    }
                    AddEditProjectScreen(
                        viewModel = viewModel, onBack = {
                            if (navStack.size > 1) navStack.removeAt(navStack.size - 1)
                        }, modifier = modifier
                    )
                }

                currentRoute.startsWith("project_detail/") -> {
                    val viewModel = koinViewModel<ProjectDetailViewModel>()
                    val pId = currentProjectId ?: ""
                    LaunchedEffect(pId) {
                        viewModel.setProjectId(pId)
                    }
                    ProjectDetailScreen(
                        viewModel = viewModel, onBack = {
                        if (navStack.size > 1) navStack.removeAt(navStack.size - 1)
                    }, onEditProject = { projectId ->
                        currentProjectId = projectId
                        navigateTo(Screen.AddEditProject.createRoute(projectId))
                    }, onAddSubtask = { projectId ->
                        currentProjectId = projectId
                        currentSubtaskId = null
                        navigateTo(Screen.AddEditSubtask.createRoute(projectId, null))
                    }, onEditSubtask = { projectId, subtaskId ->
                        currentProjectId = projectId
                        currentSubtaskId = subtaskId
                        navigateTo(Screen.AddEditSubtask.createRoute(projectId, subtaskId))
                    }, modifier = modifier
                    )
                }

                currentRoute.startsWith("add_edit_subtask") -> {
                    val viewModel = koinViewModel<AddEditSubtaskViewModel>()
                    val pId = currentProjectId ?: ""
                    val sId = currentSubtaskId
                    LaunchedEffect(pId, sId) {
                        viewModel.initSubtask(pId, sId)
                    }
                    AddEditSubtaskScreen(
                        viewModel = viewModel, onBack = {
                            if (navStack.size > 1) navStack.removeAt(navStack.size - 1)
                        }, modifier = modifier
                    )
                }

                currentRoute == Screen.Reports.route -> {
                    val viewModel = koinViewModel<ReportsViewModel>()
                    ReportsScreen(viewModel = viewModel, modifier = modifier)
                }

                currentRoute == Screen.Settings.route -> {
                    val viewModel = koinViewModel<SettingsViewModel>()
                    SettingsScreen(
                        viewModel = viewModel, onBackClick = {
                        if (navStack.size > 1) {
                            navStack.removeAt(navStack.size - 1)
                        } else {
                            navStack.clear()
                            navStack.add(Screen.Dashboard.route)
                        }
                    }, onNavigateToBackup = {
                        // Maps directly to your custom state stack transition handler
                        // navigateTo(Screen.Backup.route)
                    }, onNavigateToAbout = {
                        // Maps directly to your custom state stack transition handler
                        //navigateTo(Screen.About.route)
                    }, modifier = modifier
                    )
                }

                else -> {
                    val viewModel = koinViewModel<DashboardViewModel>()
                    DashboardScreen(
                        viewModel = viewModel,
                        onNavigateToProjectDetail = { projectId ->
                            currentProjectId = projectId
                            navigateTo(Screen.ProjectDetail.createRoute(projectId))
                        },
                        onNavigateToAddProject = {
                            currentProjectId = null
                            navigateTo(Screen.AddEditProject.createRoute(null))
                        },
                        onNavigateToProjects = {
                            navigateTo(Screen.Projects.route)
                        },
                        modifier = modifier
                    )
                }
            }
        }
    }
}
