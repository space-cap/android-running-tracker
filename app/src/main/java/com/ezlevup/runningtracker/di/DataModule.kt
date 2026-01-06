package com.ezlevup.runningtracker.di

import androidx.room.Room
import com.ezlevup.runningtracker.data.local.RunningDatabase
import com.ezlevup.runningtracker.data.repository.RunningRepositoryImpl
import com.ezlevup.runningtracker.domain.repository.RunningRepository
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val dataModule = module {
    // Room Database
    single {
        Room.databaseBuilder(androidApplication(), RunningDatabase::class.java, "running_db")
                .build()
    }

    // Dao
    single { get<RunningDatabase>().getRunDao() }

    // Repository
    single<RunningRepository> { RunningRepositoryImpl(get()) }
}
