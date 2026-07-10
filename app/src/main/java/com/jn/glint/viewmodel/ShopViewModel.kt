package com.jn.glint.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.glint.billing.BillingManager
import com.jn.glint.billing.StoreProduct
import com.jn.glint.model.CoinRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

class ShopViewModel(
    application: Application,
    coinRepository: CoinRepository
) : ViewModel() {
    private val billingManager = BillingManager(application, coinRepository)

    val products: StateFlow<List<StoreProduct>> = billingManager.products
    val userCoins: StateFlow<Int> = coinRepository.coinsFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = 0
    )

    fun buyProduct(activity: Activity, product: StoreProduct) {
        billingManager.launchBillingFlow(activity, product)
    }

    override fun onCleared() {
        super.onCleared()
        billingManager.endConnection()
    }
}
