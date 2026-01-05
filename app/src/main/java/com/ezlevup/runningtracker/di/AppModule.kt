package com.ezlevup.runningtracker.di

import android.content.Context
import androidx.room.Room
import com.ezlevup.runningtracker.data.local.RunningDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRunningDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        RunningDatabase::class.java,
        "running_db"
    ).build()

    @Singleton
    @Provides
    fun provideRunningDao(db: RunningDatabase) = db.getRunningDao()
}
