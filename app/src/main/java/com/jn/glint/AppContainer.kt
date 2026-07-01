package com.jn.glint

import android.content.Context
import com.jn.glint.data.repository.GameRepositoryImpl
import com.jn.glint.domain.repository.GameRepository
import com.jn.glint.domain.usecase.GenerateTilesUseCase
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
    val gameRepository: GameRepository
    val soundManager: SoundManager

    // Use Cases
    val getUserCoinsUseCase: GetUserCoinsUseCase
    val updateUserCoinsUseCase: UpdateUserCoinsUseCase
    val generateTilesUseCase: GenerateTilesUseCase
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

    override val gameRepository: GameRepository by lazy {
        GameRepositoryImpl()
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

    override val generateTilesUseCase: GenerateTilesUseCase by lazy {
        GenerateTilesUseCase(gameRepository)
    }
}
