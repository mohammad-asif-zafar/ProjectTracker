package com.hathway.projecttracker.domain.repository

import com.hathway.projecttracker.domain.model.Subtask
import kotlinx.coroutines.flow.Flow

interface SubtaskRepository {
    fun observeSubtasks(projectId: String): Flow<List<Subtask>>
    suspend fun getSubtasks(projectId: String): List<Subtask>
    suspend fun getSubtaskById(id: String): Subtask?
    suspend fun addSubtask(subtask: Subtask)
    suspend fun updateSubtask(subtask: Subtask)
    suspend fun deleteSubtask(id: String)
}
