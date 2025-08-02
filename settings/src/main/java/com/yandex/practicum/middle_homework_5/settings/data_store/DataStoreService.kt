package com.yandex.practicum.middle_homework_5.settings.data_store

import kotlinx.coroutines.flow.StateFlow

interface DataStoreService {
    val settingData: StateFlow<SettingContainer>
    suspend fun saveSetting(periodic: Long, delayed: Long)
    suspend fun readSetting()
}