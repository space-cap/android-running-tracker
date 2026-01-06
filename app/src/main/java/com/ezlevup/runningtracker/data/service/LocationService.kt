package com.ezlevup.runningtracker.data.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.NotificationManager.IMPORTANCE_LOW
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Build
import android.os.Looper
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
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import timber.log.Timber

typealias Polyline = MutableList<LatLng>

typealias Polylines = MutableList<Polyline>

class LocationService : LifecycleService() {

    private var isFirstRun = true
    private var serviceKilled = false

    // Koin을 통해 주입받을 예정입니다.
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
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        isTracking.observe(this) { updateLocationTracking(it) }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        intent?.let {
            when (it.action) {
                ACTION_START_OR_RESUME_SERVICE -> {
                    if (isFirstRun) {
                        startForegroundService()
                        isFirstRun = false
                    } else {
                        Timber.d("Resuming service...")
                        startTimer()
                    }
                }
                ACTION_PAUSE_SERVICE -> {
                    Timber.d("Paused service")
                    pauseService()
                }
                ACTION_STOP_SERVICE -> {
                    Timber.d("Stopped service")
                    killService()
                }
            }
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun pauseService() {
        isTracking.postValue(false)
    }

    private fun killService() {
        serviceKilled = true
        isFirstRun = true
        pauseService()
        postInitialValues()
        stopForeground(true)
        stopSelf()
    }

    // 간단한 위치 업데이트 로직
    @SuppressLint("MissingPermission")
    private fun updateLocationTracking(isTracking: Boolean) {
        if (isTracking) {
            val request =
                    LocationRequest.Builder(
                                    Priority.PRIORITY_HIGH_ACCURACY,
                                    LOCATION_UPDATE_INTERVAL
                            )
                            .setMinUpdateIntervalMillis(FASTEST_LOCATION_INTERVAL)
                            .build()
            fusedLocationProviderClient.requestLocationUpdates(
                    request,
                    locationCallback,
                    Looper.getMainLooper()
            )
        } else {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    private val locationCallback =
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
                    if (isTracking.value == true) {
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

    private fun addPathPoint(location: Location?) {
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
        startTimer()
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
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentTitle("Running Tracker")
                        .setContentText("00:00:00")

        startForeground(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun startTimer() {
        addEmptyPolyline()
        isTracking.postValue(true)
        // 실제 타이머 로직은 4단계에서 고도화할 예정입니다.
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
