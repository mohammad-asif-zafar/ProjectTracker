package com.hathway.projecttracker.domain.usecase.subtask

import com.hathway.projecttracker.domain.model.Subtask
import com.hathway.projecttracker.domain.repository.SubtaskRepository
import kotlinx.coroutines.flow.Flow

class GetSubtasksForProjectUseCase(private val repository: SubtaskRepository) {
    operator fun invoke(projectId: String): Flow<List<Subtask>> = repository.observeSubtasks(projectId)
}
