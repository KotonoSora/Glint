package com.kotonosora.glint.model

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class CoinRepository(private val context: Context) {
    private val COINS_KEY = intPreferencesKey("coins_balance")

    val coinsFlow: Flow<Int> = context.dataStore.data
        .map { preferences ->
            preferences[COINS_KEY] ?: 100 // Default 100 coins
        }

    suspend fun updateCoins(amount: Int) {
        context.dataStore.edit { preferences ->
            val current = preferences[COINS_KEY] ?: 100
            preferences[COINS_KEY] = current + amount
        }
    }
}
