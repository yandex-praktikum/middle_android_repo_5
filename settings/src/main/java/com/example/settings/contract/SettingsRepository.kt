package com.example.settings.contract

import kotlinx.coroutines.flow.StateFlow

interface SettingsRepository {
    val settingData: StateFlow<com.example.settings.data_store.SettingContainer>
    suspend fun saveSetting(periodic: Long, delayed: Long)
    suspend fun readSetting()
}