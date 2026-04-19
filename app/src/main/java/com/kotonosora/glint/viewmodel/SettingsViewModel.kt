package com.kotonosora.glint.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.kotonosora.glint.model.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = SettingsRepository(application)

    val soundEnabled: StateFlow<Boolean> = repository.soundEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    val musicEnabled: StateFlow<Boolean> = repository.musicEnabledFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), true)

    fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setSoundEnabled(enabled)
        }
    }

    fun setMusicEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.setMusicEnabled(enabled)
        }
    }
}
