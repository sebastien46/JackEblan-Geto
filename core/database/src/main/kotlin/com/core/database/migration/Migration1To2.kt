package com.core.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

class Migration1To2 : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {
        // Create a new table with the same structure but with an added id column
        db.execSQL(
            """
           CREATE TABLE IF NOT EXISTS new_table (
               enabled INTEGER NOT NULL,
               settingsType TEXT NOT NULL,
               packageName TEXT NOT NULL,
               label TEXT NOT NULL,
               key TEXT NOT NULL,
               valueOnLaunch TEXT NOT NULL,
               valueOnRevert TEXT NOT NULL,
               PRIMARY KEY(key)
           )
       """.trimIndent()
        )

        // Copy the data from the old table to the new table
        db.execSQL("INSERT INTO new_table (enabled, settingsType, packageName, label, key, valueOnLaunch, valueOnRevert) SELECT * FROM UserAppSettingsItemEntity")

        // Delete the old table
        db.execSQL("DROP TABLE UserAppSettingsItemEntity")

        // Rename the new table to the old table name
        db.execSQL("ALTER TABLE new_table RENAME TO AppSettingsItemEntity")
    }
}