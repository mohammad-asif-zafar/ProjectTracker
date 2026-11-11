package com.hathway.projecttracker

import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.model.Subtask
import com.hathway.projecttracker.domain.usecase.progress.CalculateProjectProgressUseCase
import kotlin.test.Test
import kotlin.test.assertEquals

class ProgressCalculationTest {

    private val calculator = CalculateProjectProgressUseCase()

    @Test
    fun testSubtaskEmptyProgressIsZeroAndPending() {
        val subtasks = emptyList<Subtask>()
        val progress = calculator.calculateProgress(subtasks)
        assertEquals(0, progress)
        assertEquals(ProjectStatus.PENDING, ProjectStatus.fromProgress(progress))
    }

    @Test
    fun testSubtaskProgressCalculationAndStatusRules() {
        val subtasks = listOf(
            Subtask("1", "p1", "Task 1", "", 100, ProjectStatus.DONE),
            Subtask("2", "p1", "Task 2", "", 0, ProjectStatus.PENDING)
        )
        // 100 + 0 = 100 / 2 = 50%
        val progress = calculator.calculateProgress(subtasks)
        assertEquals(50, progress)
        assertEquals(ProjectStatus.IN_PROGRESS, ProjectStatus.fromProgress(progress))
    }

    @Test
    fun testCompleteProjectProgress() {
        val subtasks = listOf(
            Subtask("1", "p1", "Task 1", "", 100, ProjectStatus.DONE),
            Subtask("2", "p1", "Task 2", "", 100, ProjectStatus.DONE)
        )
        val progress = calculator.calculateProgress(subtasks)
        assertEquals(100, progress)
        assertEquals(ProjectStatus.DONE, ProjectStatus.fromProgress(progress))
    }

    @Test
    fun testOverallProjectProgressAggregation() {
        val projects = listOf(
            Project("p1", "Project 1", "P1", emptyList(), "", "📁", 100, ProjectStatus.DONE, emptyList()),
            Project("p2", "Project 2", "P2", emptyList(), "", "📁", 0, ProjectStatus.PENDING, emptyList())
        )
        val summary = calculator.calculateProjectProgress(projects)
        assertEquals(2, summary.totalProjects)
        assertEquals(1, summary.completedProjects)
        assertEquals(1, summary.pendingProjects)
        assertEquals(50, summary.overallCompletion)
    }

    @Test
    fun testUniqueIdGeneration() {
        val projId = "proj-${kotlin.uuid.Uuid.random()}"
        val subId = "sub-${kotlin.uuid.Uuid.random()}"
        kotlin.test.assertTrue(projId.startsWith("proj-"))
        kotlin.test.assertTrue(subId.startsWith("sub-"))
        kotlin.test.assertNotEquals(projId, "proj-${kotlin.uuid.Uuid.random()}")
    }

    @Test
    fun testPartialSubtaskProgress() {
        val subtasks = listOf(
            Subtask("1", "p1", "Task 1", "", 30, ProjectStatus.IN_PROGRESS),
            Subtask("2", "p1", "Task 2", "", 70, ProjectStatus.IN_PROGRESS)
        )
        val progress = calculator.calculateProgress(subtasks)
        assertEquals(50, progress)
        assertEquals(ProjectStatus.IN_PROGRESS, ProjectStatus.fromProgress(progress))
    }
}
