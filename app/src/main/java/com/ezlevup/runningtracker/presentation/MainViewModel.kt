package com.ezlevup.runningtracker.presentation.home

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModel
import com.ezlevup.runningtracker.data.service.LocationService
import com.ezlevup.runningtracker.domain.repository.RunningRepository
import com.ezlevup.runningtracker.util.Constants.ACTION_PAUSE_SERVICE
import com.ezlevup.runningtracker.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.ezlevup.runningtracker.util.Constants.ACTION_STOP_SERVICE

class MainViewModel(private val runningRepository: RunningRepository) : ViewModel() {

    val isTracking = LocationService.isTracking
    val pathPoints = LocationService.pathPoints

    fun startOrResumeService(context: Context) {
        sendCommandToService(context, ACTION_START_OR_RESUME_SERVICE)
    }

    fun pauseService(context: Context) {
        sendCommandToService(context, ACTION_PAUSE_SERVICE)
    }

    fun stopService(context: Context) {
        sendCommandToService(context, ACTION_STOP_SERVICE)
    }

    private fun sendCommandToService(context: Context, action: String) {
        Intent(context, LocationService::class.java).also {
            it.action = action
            context.startService(it)
        }
    }
}
