package com.jn.glint.domain.usecase

import com.jn.glint.model.CoinRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use case to get the user's current coins balance.
 */
class GetUserCoinsUseCase(
    private val coinRepository: CoinRepository
) {
    operator fun invoke(): Flow<Int> = coinRepository.coinsFlow
}
