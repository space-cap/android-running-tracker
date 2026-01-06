package com.ezlevup.runningtracker.data.repository

import com.ezlevup.runningtracker.data.local.RunEntity
import com.ezlevup.runningtracker.data.local.RunningDao
import com.ezlevup.runningtracker.domain.repository.RunningRepository
import kotlinx.coroutines.flow.Flow

class RunningRepositoryImpl(private val runningDao: RunningDao) : RunningRepository {

    override suspend fun insertRun(run: RunEntity) = runningDao.insertRun(run)

    override suspend fun deleteRun(run: RunEntity) = runningDao.deleteRun(run)

    override fun getAllRunsSortedByDate(): Flow<List<RunEntity>> =
            runningDao.getAllRunsSortedByDate()

    override fun getAllRunsSortedByTimeInMillis(): Flow<List<RunEntity>> =
            runningDao.getAllRunsSortedByTimeInMillis()

    override fun getAllRunsSortedByCaloriesBurned(): Flow<List<RunEntity>> =
            runningDao.getAllRunsSortedByCaloriesBurned()

    override fun getAllRunsSortedByAvgSpeed(): Flow<List<RunEntity>> =
            runningDao.getAllRunsSortedByAvgSpeed()

    override fun getAllRunsSortedByDistance(): Flow<List<RunEntity>> =
            runningDao.getAllRunsSortedByDistance()

    override fun getTotalTimeInMillis(): Flow<Long> = runningDao.getTotalTimeInMillis()

    override fun getTotalCaloriesBurned(): Flow<Int> = runningDao.getTotalCaloriesBurned()

    override fun getTotalDistance(): Flow<Int> = runningDao.getTotalDistance()

    override fun getTotalAvgSpeed(): Flow<Float> = runningDao.getTotalAvgSpeed()
}
