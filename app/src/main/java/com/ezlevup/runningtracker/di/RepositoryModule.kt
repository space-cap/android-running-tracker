package com.ezlevup.runningtracker.di

import com.ezlevup.runningtracker.data.repository.RunningRepositoryImpl
import com.ezlevup.runningtracker.domain.repository.RunningRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRunningRepository(
        runningRepositoryImpl: RunningRepositoryImpl
    ): RunningRepository
}
