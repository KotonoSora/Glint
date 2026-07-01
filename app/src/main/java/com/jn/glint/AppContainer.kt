package com.jn.glint

import android.content.Context
import com.jn.glint.domain.usecase.GetUserCoinsUseCase
import com.jn.glint.domain.usecase.UpdateUserCoinsUseCase
import com.jn.glint.model.CoinRepository
import com.jn.glint.model.SettingsRepository
import com.jn.glint.ui.SoundManager

/**
 * Dependency container for the application.
 */
interface AppContainer {
    val coinRepository: CoinRepository
    val settingsRepository: SettingsRepository
    val soundManager: SoundManager

    // Use Cases
    val getUserCoinsUseCase: GetUserCoinsUseCase
    val updateUserCoinsUseCase: UpdateUserCoinsUseCase
}

/**
 * Implementation of the [AppContainer].
 */
class AppContainerImpl(private val context: Context) : AppContainer {

    override val coinRepository: CoinRepository by lazy {
        CoinRepository(context)
    }

    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(context)
    }

    override val soundManager: SoundManager by lazy {
        SoundManager(context)
    }

    override val getUserCoinsUseCase: GetUserCoinsUseCase by lazy {
        GetUserCoinsUseCase(coinRepository)
    }

    override val updateUserCoinsUseCase: UpdateUserCoinsUseCase by lazy {
        UpdateUserCoinsUseCase(coinRepository)
    }
}
