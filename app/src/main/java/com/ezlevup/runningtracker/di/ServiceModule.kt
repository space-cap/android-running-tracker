package com.ezlevup.runningtracker.di

import com.google.android.gms.location.LocationServices
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val serviceModule = module {
    single { LocationServices.getFusedLocationProviderClient(androidApplication()) }
}
