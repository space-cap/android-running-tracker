package com.ezlevup.runningtracker

import android.app.Application
import com.ezlevup.runningtracker.di.dataModule
import com.ezlevup.runningtracker.di.serviceModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RunningTrackerApp : Application() {
    override fun onCreate() {
        super.onCreate()

        // Timber 초기화
        Timber.plant(Timber.DebugTree())

        // Koin 초기화
        startKoin {
            androidLogger()
            androidContext(this@RunningTrackerApp)
            modules(dataModule, serviceModule)
        }
    }
}
