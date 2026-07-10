package com.jn.glint.domain.usecase

import com.jn.glint.model.CoinRepository

/**
 * Use case to update the user's coins balance.
 */
class UpdateUserCoinsUseCase(
    private val coinRepository: CoinRepository
) {
    suspend operator fun invoke(amount: Int) {
        coinRepository.updateCoins(amount)
    }
}
