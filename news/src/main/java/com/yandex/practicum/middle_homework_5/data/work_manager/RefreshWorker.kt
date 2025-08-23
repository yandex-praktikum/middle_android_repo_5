package com.yandex.practicum.middle_homework_5.data.work_manager


import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.yandex.practicum.middle_homework_5.ui.NewsViewModel
import org.koin.core.context.GlobalContext.get


class RefreshWorker(
    context: Context,
    workParams: WorkerParameters
) : CoroutineWorker(context, workParams) {

    private val viewModel: NewsViewModel by get().inject()

    override suspend fun doWork(): Result {
        viewModel.refreshData()
        return Result.success()
    }
}