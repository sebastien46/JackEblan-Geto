package com.core.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration2To3 : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create a new table with the same structure but with an added id column
        db.execSQL(
            """
           CREATE TABLE IF NOT EXISTS new_table (
               id INTEGER PRIMARY KEY AUTOINCREMENT,
               enabled INTEGER NOT NULL,
               settingsType TEXT NOT NULL,
               packageName TEXT NOT NULL,
               label TEXT NOT NULL,
               key TEXT NOT NULL,
               valueOnLaunch TEXT NOT NULL,
               valueOnRevert TEXT NOT NULL
           )
       """.trimIndent()
        )

        // Copy the data from the old table to the new table
        db.execSQL("INSERT INTO new_table (enabled, settingsType, packageName, label, key, valueOnLaunch, valueOnRevert) SELECT * FROM AppSettingsItemEntity")

        // Delete the old table
        db.execSQL("DROP TABLE AppSettingsItemEntity")

        // Rename the new table to the old table name
        db.execSQL("ALTER TABLE new_table RENAME TO AppSettingsItemEntity")
    }
}