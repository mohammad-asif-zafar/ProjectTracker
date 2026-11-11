package com.hathway.projecttracker

import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.hathway.projecttracker.data.db.ProjectDatabase
import com.hathway.projecttracker.data.local.ProjectDataSource
import com.hathway.projecttracker.domain.model.Project
import com.hathway.projecttracker.domain.model.ProjectStatus
import com.hathway.projecttracker.domain.model.Subtask
import com.hathway.projecttracker.domain.usecase.progress.CalculateProjectProgressUseCase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.io.path.createTempFile
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ProjectDataSourceTest {

    private val progressCalculator = CalculateProjectProgressUseCase()

    private fun createTestDatabase(): ProjectDatabase {
        val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        ProjectDatabase.Schema.create(driver)
        driver.execute(null, "PRAGMA foreign_keys=ON;", 0)
        return ProjectDatabase(driver)
    }

    @Test
    fun testAddAndGetProject() = runTest {
        val database = createTestDatabase()
        val dataSource = ProjectDataSource(database, progressCalculator)

        val project = Project(
            id = "p1",
            name = "Test Project",
            shortName = "TP",
            keywords = listOf("test", "kmp"),
            description = "A test project",
            icon = "folder",
            progress = 0,
            status = ProjectStatus.PENDING,
            subtasks = emptyList()
        )

        dataSource.addProject(project)

        val fetched = dataSource.getProjectById("p1")
        assertNotNull(fetched)
        assertEquals("Test Project", fetched.name)
        assertEquals(listOf("test", "kmp"), fetched.keywords)
        assertEquals(ProjectStatus.PENDING, fetched.status)
    }

    @Test
    fun testCascadeDeletion() = runTest {
        val database = createTestDatabase()
        val dataSource = ProjectDataSource(database, progressCalculator)

        val subtask = Subtask(
            id = "s1",
            projectId = "p1",
            title = "Subtask 1",
            description = "Desc",
            progress = 0,
            status = ProjectStatus.PENDING
        )

        val project = Project(
            id = "p1",
            name = "Test Project",
            shortName = "TP",
            keywords = emptyList(),
            description = "A test project",
            icon = "folder",
            progress = 0,
            status = ProjectStatus.PENDING,
            subtasks = listOf(subtask)
        )

        dataSource.addProject(project)
        assertNotNull(dataSource.getProjectById("p1"))
        assertEquals(1, dataSource.getProjectById("p1")?.subtasks?.size)

        dataSource.deleteProject("p1")

        assertNull(dataSource.getProjectById("p1"))
        val subtasks = database.subtaskEntityQueries.selectSubtasksForProject("p1").executeAsList()
        assertTrue(subtasks.isEmpty(), "Subtasks should be cascade-deleted when project is deleted")
    }

    @Test
    fun testForeignKeyCascadeDeletionWithoutManualDelete() = runTest {
        val database = createTestDatabase()
        val subtask = Subtask(
            id = "s1",
            projectId = "p1",
            title = "Subtask 1",
            description = "Desc",
            progress = 0,
            status = ProjectStatus.PENDING
        )
        val project = Project(
            id = "p1",
            name = "Test Project",
            shortName = "TP",
            keywords = emptyList(),
            description = "A test project",
            icon = "folder",
            progress = 0,
            status = ProjectStatus.PENDING,
            subtasks = listOf(subtask)
        )
        val dataSource = ProjectDataSource(database, progressCalculator)
        dataSource.addProject(project)

        assertNotNull(database.projectEntityQueries.selectById("p1").executeAsOneOrNull())
        assertEquals(1, database.subtaskEntityQueries.selectSubtasksForProject("p1").executeAsList().size)

        // Call projectQueries.deleteProject("p1") directly without manually calling subtaskQueries.deleteSubtasksForProject("p1")
        database.transaction {
            database.projectEntityQueries.deleteProject("p1")
        }

        assertNull(database.projectEntityQueries.selectById("p1").executeAsOneOrNull())
        val remainingSubtasks = database.subtaskEntityQueries.selectSubtasksForProject("p1").executeAsList()
        assertTrue(remainingSubtasks.isEmpty(), "Subtasks must be automatically deleted via SQLite ON DELETE CASCADE")
    }

    @Test
    fun testFileBackedPersistence() = runTest {
        val tempFile = createTempFile(prefix = "project_persist_test_", suffix = ".db")
        tempFile.toFile().deleteOnExit()
        val url = "jdbc:sqlite:${tempFile.toAbsolutePath()}"

        // 1. Open driver, create schema, insert project & subtask
        val driver1 = JdbcSqliteDriver(url)
        ProjectDatabase.Schema.create(driver1)
        driver1.execute(null, "PRAGMA foreign_keys=ON;", 0)
        val db1 = ProjectDatabase(driver1)
        val dataSource1 = ProjectDataSource(db1, progressCalculator)

        val subtask = Subtask(
            id = "s1",
            projectId = "p1",
            title = "Persistent Subtask",
            description = "Desc",
            progress = 25,
            status = ProjectStatus.IN_PROGRESS
        )
        val project = Project(
            id = "p1",
            name = "Persistent Project",
            shortName = "PP",
            keywords = listOf("persist"),
            description = "File-backed test",
            icon = "database",
            progress = 0,
            status = ProjectStatus.PENDING,
            subtasks = listOf(subtask)
        )
        dataSource1.addProject(project)
        driver1.close()

        // 2. Open new driver on same file, assert data exists
        val driver2 = JdbcSqliteDriver(url)
        driver2.execute(null, "PRAGMA foreign_keys=ON;", 0)
        val db2 = ProjectDatabase(driver2)
        val dataSource2 = ProjectDataSource(db2, progressCalculator)

        val fetched1 = dataSource2.getProjectById("p1")
        assertNotNull(fetched1)
        assertEquals("Persistent Project", fetched1.name)
        assertEquals(1, fetched1.subtasks.size)
        assertEquals("Persistent Subtask", fetched1.subtasks[0].title)
        assertEquals(25, fetched1.subtasks[0].progress)

        // 3. Update a subtask
        val updatedSubtask = fetched1.subtasks[0].copy(progress = 100, status = ProjectStatus.DONE)
        dataSource2.updateSubtask(updatedSubtask)
        driver2.close()

        // 4. Close and reopen again, verify updated values persist
        val driver3 = JdbcSqliteDriver(url)
        driver3.execute(null, "PRAGMA foreign_keys=ON;", 0)
        val db3 = ProjectDatabase(driver3)
        val dataSource3 = ProjectDataSource(db3, progressCalculator)

        val fetched2 = dataSource3.getProjectById("p1")
        assertNotNull(fetched2)
        assertEquals(100, fetched2.subtasks[0].progress)
        assertEquals(ProjectStatus.DONE, fetched2.subtasks[0].status)
        assertEquals(100, fetched2.progress)

        driver3.close()
        tempFile.toFile().delete()
    }

    @Test
    fun testFlowReactivity() = runTest {
        val database = createTestDatabase()
        val dataSource = ProjectDataSource(database, progressCalculator)

        val projectsBefore = dataSource.projectsFlow.first()
        assertTrue(projectsBefore.isEmpty())

        val subtask = Subtask(
            id = "s1",
            projectId = "p2",
            title = "Task",
            description = "Desc",
            progress = 50,
            status = ProjectStatus.IN_PROGRESS
        )

        val project = Project(
            id = "p2",
            name = "Reactive Project",
            shortName = "RP",
            keywords = listOf("flow"),
            description = "Reactive test",
            icon = "star",
            progress = 0,
            status = ProjectStatus.PENDING,
            subtasks = listOf(subtask)
        )

        dataSource.addProject(project)

        val projectsAfter = dataSource.projectsFlow.first()
        assertEquals(1, projectsAfter.size)
        assertEquals("Reactive Project", projectsAfter[0].name)
        assertEquals(ProjectStatus.IN_PROGRESS, projectsAfter[0].status)
    }

    @Test
    fun testSubtaskDeletionRecalculatesProgress() = runTest {
        val database = createTestDatabase()
        val dataSource = ProjectDataSource(database, progressCalculator)

        val subtask1 = Subtask(
            id = "s1",
            projectId = "p1",
            title = "Task 1",
            description = "Desc",
            progress = 100,
            status = ProjectStatus.DONE
        )
        val subtask2 = Subtask(
            id = "s2",
            projectId = "p1",
            title = "Task 2",
            description = "Desc",
            progress = 0,
            status = ProjectStatus.PENDING
        )

        val project = Project(
            id = "p1",
            name = "Project",
            shortName = "P",
            keywords = emptyList(),
            description = "Test",
            icon = "📁",
            progress = 0,
            status = ProjectStatus.PENDING,
            subtasks = listOf(subtask1, subtask2)
        )

        dataSource.addProject(project)
        val pBefore = dataSource.getProjectById("p1")
        assertNotNull(pBefore)
        assertEquals(50, pBefore.progress)
        assertEquals(ProjectStatus.IN_PROGRESS, pBefore.status)
        assertEquals(2, pBefore.subtasks.size)

        // Delete subtask2 (progress 0)
        dataSource.deleteSubtask("s2")

        val pAfter = dataSource.getProjectById("p1")
        assertNotNull(pAfter)
        assertEquals(100, pAfter.progress)
        assertEquals(ProjectStatus.DONE, pAfter.status)
        assertEquals(1, pAfter.subtasks.size)
        assertEquals("s1", pAfter.subtasks[0].id)
    }
}
