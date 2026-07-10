package com.jn.glint.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.jn.glint.AppContainer
import com.jn.glint.GlintApplication

/**
 * Factory for ViewModels that provides dependencies from the [AppContainer].
 */
class ViewModelFactory(
    private val container: AppContainer,
    private val application: GlintApplication
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(GameViewModel::class.java) -> {
                GameViewModel(
                    container.getUserCoinsUseCase,
                    container.updateUserCoinsUseCase,
                    container.generateTilesUseCase
                ) as T
            }

            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(container.settingsRepository) as T
            }

            modelClass.isAssignableFrom(ShopViewModel::class.java) -> {
                ShopViewModel(application, container.coinRepository) as T
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
                val application =
                    checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as GlintApplication
                val container = application.container
                return when {
                    modelClass.isAssignableFrom(GameViewModel::class.java) -> {
                        GameViewModel(
                            container.getUserCoinsUseCase,
                            container.updateUserCoinsUseCase,
                            container.generateTilesUseCase
                        ) as T
                    }

                    modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                        SettingsViewModel(container.settingsRepository) as T
                    }

                    modelClass.isAssignableFrom(ShopViewModel::class.java) -> {
                        ShopViewModel(application, container.coinRepository) as T
                    }

                    else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
        }
    }
}
