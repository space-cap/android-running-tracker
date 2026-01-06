package com.ezlevup.runningtracker.domain.repository

import com.ezlevup.runningtracker.data.local.RunEntity
import kotlinx.coroutines.flow.Flow

interface RunningRepository {

    suspend fun insertRun(run: RunEntity)

    suspend fun deleteRun(run: RunEntity)

    fun getAllRunsSortedByDate(): Flow<List<RunEntity>>

    fun getAllRunsSortedByTimeInMillis(): Flow<List<RunEntity>>

    fun getAllRunsSortedByCaloriesBurned(): Flow<List<RunEntity>>

    fun getAllRunsSortedByAvgSpeed(): Flow<List<RunEntity>>

    fun getAllRunsSortedByDistance(): Flow<List<RunEntity>>

    fun getTotalTimeInMillis(): Flow<Long>

    fun getTotalCaloriesBurned(): Flow<Int>

    fun getTotalDistance(): Flow<Int>

    fun getTotalAvgSpeed(): Flow<Float>
}
