package com.ezlevup.runningtracker.data.local

import androidx.room.*

@Database(
    entities = [RunEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RunningDatabase : RoomDatabase() {
    abstract fun getRunningDao(): RunningDao
}
