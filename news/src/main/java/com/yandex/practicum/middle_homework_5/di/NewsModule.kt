package com.yandex.practicum.middle_homework_5.di

import androidx.room.Room
import com.yandex.practicum.middle_homework_5.data.database.NewsDatabase
import com.yandex.practicum.middle_homework_5.data.news_service.NewsServiceImpl
import com.yandex.practicum.middle_homework_5.data.work_manager.WorkManagerServiceImp
import com.yandex.practicum.middle_homework_5.ui.NewsViewModel
import com.yandex.practicum.middle_homework_5.ui.contract.NewsService
import com.yandex.practicum.middle_homework_5.ui.contract.WorkManagerService
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val newsModule = module {
    single<NewsService> {
        NewsServiceImpl(
            application = androidContext(),
            fileName = "runews.json"
        )
    }
    single<NewsDatabase> {
        Room.databaseBuilder(
            androidContext(),
            NewsDatabase::class.java,
            "news_database"
        )
            .build()
    }
    single<WorkManagerService> { WorkManagerServiceImp(androidApplication(), get()) }
    single<NewsViewModel> { NewsViewModel(get(), get(), get()) }
}