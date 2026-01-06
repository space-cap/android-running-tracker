package com.ezlevup.runningtracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [RunEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RunningDatabase : RoomDatabase() {
    abstract fun getRunDao(): RunningDao
}
