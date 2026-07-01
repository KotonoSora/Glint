package com.jn.glint.viewmodel

import android.app.Activity
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.jn.glint.billing.BillingManager
import com.jn.glint.billing.StoreProduct
import com.jn.glint.model.CoinRepository
import kotlinx.coroutines.flow.StateFlow

class ShopViewModel(application: Application) : AndroidViewModel(application) {
    private val coinRepository = CoinRepository(application)
    private val billingManager = BillingManager(application, coinRepository)

    val products: StateFlow<List<StoreProduct>> = billingManager.products

    fun buyProduct(activity: Activity, product: StoreProduct) {
        billingManager.launchBillingFlow(activity, product)
    }

    override fun onCleared() {
        super.onCleared()
        billingManager.endConnection()
    }
}
