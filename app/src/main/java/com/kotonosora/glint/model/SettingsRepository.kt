package com.kotonosora.glint.model

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "game_settings")

class SettingsRepository(private val context: Context) {
    private val SOUND_ENABLED_KEY = booleanPreferencesKey("sound_enabled")
    private val MUSIC_ENABLED_KEY = booleanPreferencesKey("music_enabled")

    val soundEnabledFlow: Flow<Boolean> = context.settingsDataStore.data
        .map { it[SOUND_ENABLED_KEY] ?: true }

    val musicEnabledFlow: Flow<Boolean> = context.settingsDataStore.data
        .map { it[MUSIC_ENABLED_KEY] ?: true }

    suspend fun setSoundEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { it[SOUND_ENABLED_KEY] = enabled }
    }

    suspend fun setMusicEnabled(enabled: Boolean) {
        context.settingsDataStore.edit { it[MUSIC_ENABLED_KEY] = enabled }
    }
}
