package com.yandex.practicum.middle_homework_5

import android.app.Application
import com.yandex.practicum.middle_homework_5.di.newsModule
import com.yandex.practicum.middle_homework_5.settings.di.settingsModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(newsModule, settingsModule)
        }
    }
}