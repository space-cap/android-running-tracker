package com.ezlevup.runningtracker.data.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.MutableLiveData
import com.ezlevup.runningtracker.R
import com.ezlevup.runningtracker.util.Constants.ACTION_PAUSE_SERVICE
import com.ezlevup.runningtracker.util.Constants.ACTION_START_OR_RESUME_SERVICE
import com.ezlevup.runningtracker.util.Constants.ACTION_STOP_SERVICE
import com.ezlevup.runningtracker.util.Constants.FASTEST_LOCATION_INTERVAL
import com.ezlevup.runningtracker.util.Constants.LOCATION_UPDATE_INTERVAL
import com.ezlevup.runningtracker.util.Constants.NOTIFICATION_CHANNEL_ID
import com.ezlevup.runningtracker.util.Constants.NOTIFICATION_CHANNEL_NAME
import com.ezlevup.runningtracker.util.Constants.NOTIFICATION_ID
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber

typealias Polyline = MutableList<LatLng>

typealias Polylines = MutableList<Polyline>

class LocationService : LifecycleService() {

    var isFirstRun = true
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    companion object {
        val isTracking = MutableLiveData<Boolean>()
        val pathPoints = MutableLiveData<Polylines>()
    }

    private fun postInitialValues() {
        isTracking.postValue(false)
        pathPoints.postValue(mutableListOf())
    }

    override fun onCreate() {
        super.onCreate()
        postInitialValues()
        fusedLocationProviderClient = FusedLocationProviderClient(this)

        isTracking.observe(this) { updateLocationTracking(it) }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                        Timber.d("Starting service...")
                    } else {
                        Timber.d("Resuming service...")
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    pauseService()
                    Timber.d("Paused service")
                }
                ACTION_STOP_SERVICE -> {
                    killService()
                    Timber.d("Stopped service")
                }
                else -> Unit
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun killService() {
        isFirstRun = true
        pauseService()
        postInitialValues()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            stopForeground(STOP_FOREGROUND_REMOVE)
        } else {
            stopForeground(true)
        }
        stopSelf()
    }

    private fun pauseService() {
        isTracking.postValue(false)
    }

    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            val request =
                    LocationRequest.Builder(
                                    Priority.PRIORITY_HIGH_ACCURACY,
                                    LOCATION_UPDATE_INTERVAL
                            )
                            .setMinUpdateIntervalMillis(FASTEST_LOCATION_INTERVAL)
                            .build()

            // 실제로는 권한 체크가 필요하지만, UI에서 처리됨을 가정
            try {
                fusedLocationProviderClient.requestLocationUpdates(
                        request,
                        locationCallback,
                        mainLooper
                )
            } catch (unlikely: SecurityException) {
                Timber.e("Lost location permission. Could not request updates. $unlikely")
            }
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    val locationCallback =
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    if (isTracking.value!!) {
                        result.locations.let { locations ->
                            for (location in locations) {
                                addPathPoint(location)
                                Timber.d(
                                        "NEW LOCATION: ${location.latitude}, ${location.longitude}"
                                )
                            }
                        }
                    }
                }
            }

    private fun addPathPoint(location: android.location.Location?) {
        location?.let {
            val pos = LatLng(location.latitude, location.longitude)
            pathPoints.value?.apply {
                last().add(pos)
                pathPoints.postValue(this)
            }
        }
    }

    private fun addEmptyPolyline() =
            pathPoints.value?.apply {
                add(mutableListOf())
                pathPoints.postValue(this)
            }
                    ?: pathPoints.postValue(mutableListOf(mutableListOf()))

    private fun startForegroundService() {
        addEmptyPolyline()
        isTracking.postValue(true)

        val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel(notificationManager)
        }

        val notificationBuilder =
                NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .setSmallIcon(R.drawable.ic_launcher_foreground) // 임시 아이콘
                        .setContentTitle("Running Tracker")
                        .setContentText("00:00:00")

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel(notificationManager: NotificationManager) {
        val channel =
                NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        NOTIFICATION_CHANNEL_NAME,
                        IMPORTANCE_LOW
                )
        notificationManager.createNotificationChannel(channel)
    }
}
