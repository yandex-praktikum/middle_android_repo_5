package com.yandex.practicum.middle_homework_5.settings.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.yandex.practicum.middle_homework_5.settings.data.data_store.SettingsRepositoryImpl
import com.yandex.practicum.middle_homework_5.settings.ui.SettingsViewModel
import com.yandex.practicum.middle_homework_5.settings.ui.contract.SettingsRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module
import java.io.File

val settingsModule = module {

    factory<DataStore<Preferences>> {
        provideDataStore(androidContext())
    }
    single<SettingsRepository> { SettingsRepositoryImpl(get()) }
    viewModel { SettingsViewModel(get()) }
}

fun provideDataStore(context: Context): DataStore<Preferences> {
    val name = "Application setting"
    val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        corruptionHandler = null,
        migrations = emptyList(),
        scope = CoroutineScope(Job() + Dispatchers.IO)
    ) {
        File(context.filesDir, "datastore/$name.preferences_pb")
    }
    return dataStore
}
