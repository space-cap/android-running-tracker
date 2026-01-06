package com.ezlevup.runningtracker.di

import com.ezlevup.runningtracker.presentation.history.HistoryViewModel
import com.ezlevup.runningtracker.presentation.home.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { HistoryViewModel(get()) }
}
