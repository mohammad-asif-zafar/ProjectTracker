package com.hathway.projecttracker.feature.notifications.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hathway.projecttracker.data.local.ProjectDataSource
import com.hathway.projecttracker.domain.model.ActivityItem
import kotlinx.coroutines.flow.*

data class NotificationsUiState(
    val isLoading: Boolean = true,
    val activities: List<ActivityItem> = emptyList(),
    val selectedFilter: String = "All" // "All", "Projects", "Tasks", "System"
)

class NotificationsViewModel(
    dataSource: ProjectDataSource
) : ViewModel() {

    private val _selectedFilter = MutableStateFlow("All")

    val uiState: StateFlow<NotificationsUiState> = combine(
        dataSource.activitiesFlow,
        _selectedFilter
    ) { activities, filter ->
        val filtered = when (filter) {
            "Projects" -> activities.filter { it.type == "PROJECT" }
            "Tasks" -> activities.filter { it.type == "SUBTASK" }
            "System" -> activities.filter { it.type == "SYSTEM" }
            else -> activities
        }
        NotificationsUiState(
            isLoading = false,
            activities = filtered,
            selectedFilter = filter
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = NotificationsUiState()
    )

    fun onFilterSelected(filter: String) {
        _selectedFilter.value = filter
    }
}
