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
        return driver
    }
}
