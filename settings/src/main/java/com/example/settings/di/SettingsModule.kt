package com.example.settings.di

import com.example.settings.SettingsViewModel
import com.example.settings.contract.SettingsRepository
import com.example.settings.data_store.SettingsRepositoryImpl
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<SettingsRepository> {
        SettingsRepositoryImpl(
            androidApplication()
        )
    }
    viewModel { SettingsViewModel(get()) }
}