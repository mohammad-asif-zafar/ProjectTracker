package com.hathway.projecttracker.data.db

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = NativeSqliteDriver(
            schema = ProjectDatabase.Schema,
            name = "project.db"
        )
        driver.execute(null, "PRAGMA foreign_keys=ON;", 0)

        try {
            driver.execute(null, "CREATE TABLE IF NOT EXISTS settingsEntity (key TEXT PRIMARY KEY NOT NULL, value TEXT NOT NULL);", 0)
        } catch (_: Exception) {}

        try {
            driver.execute(null, "ALTER TABLE activityEntity ADD COLUMN icon TEXT NOT NULL DEFAULT 'assignment';", 0)
        } catch (_: Exception) {}

        val columns = listOf(
            "hasActivity INTEGER NOT NULL DEFAULT 0",
            "activityDate TEXT",
            "activityStartTime TEXT",
            "activityEndTime TEXT",
            "activityDuration TEXT",
            "activityNotes TEXT"
        )
        columns.forEach { col ->
            try {
                driver.execute(null, "ALTER TABLE subtaskEntity ADD COLUMN $col;", 0)
            } catch (_: Exception) {}
        }

        return driver
    }
}
