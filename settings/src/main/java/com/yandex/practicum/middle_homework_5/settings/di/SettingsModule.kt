package com.yandex.practicum.middle_homework_5.settings.di

import com.yandex.practicum.middle_homework_5.settings.data_store.DataStoreServiceImpl
import com.yandex.practicum.middle_homework_5.settings.ui.SettingsViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val settingsModule = module {
    single<com.yandex.practicum.middle_homework_5.settings.data_store.DataStoreService> {
        DataStoreServiceImpl(
            androidApplication()
        )
    }
    viewModel { SettingsViewModel(get()) }
}