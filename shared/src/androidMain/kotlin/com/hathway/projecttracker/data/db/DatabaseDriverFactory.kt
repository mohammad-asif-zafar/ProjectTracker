package com.hathway.projecttracker.data.db

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

object AndroidContextHolder {
    lateinit var context: Context
}

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        val driver = AndroidSqliteDriver(
            schema = ProjectDatabase.Schema,
            context = AndroidContextHolder.context,
            name = "project.db"
        )
        driver.execute(null, "PRAGMA foreign_keys=ON;", 0)
        return driver
    }
}
