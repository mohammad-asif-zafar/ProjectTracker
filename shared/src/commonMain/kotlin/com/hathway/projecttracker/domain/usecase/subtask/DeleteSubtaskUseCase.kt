package com.hathway.projecttracker.domain.usecase.subtask

import com.hathway.projecttracker.domain.repository.SubtaskRepository

class DeleteSubtaskUseCase(private val repository: SubtaskRepository) {
    suspend operator fun invoke(id: String) {
        repository.deleteSubtask(id)
    }
}
