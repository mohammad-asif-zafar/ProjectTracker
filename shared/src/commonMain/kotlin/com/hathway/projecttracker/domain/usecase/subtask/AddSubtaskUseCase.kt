package com.hathway.projecttracker.domain.usecase.subtask

import com.hathway.projecttracker.domain.model.Subtask
import com.hathway.projecttracker.domain.repository.SubtaskRepository

class AddSubtaskUseCase(private val repository: SubtaskRepository) {
    suspend operator fun invoke(subtask: Subtask) {
        repository.addSubtask(subtask)
    }
}
