package com.jn.glint.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jn.glint.ui.GlintTopBar
import com.jn.glint.ui.SmallNeonButton
import com.jn.glint.ui.theme.CoinGold
import com.jn.glint.ui.theme.GlintTheme
import com.jn.glint.ui.theme.NeonCyan
import com.jn.glint.ui.theme.NeonMagenta
import com.jn.glint.viewmodel.ShopViewModel
import com.jn.glint.viewmodel.ViewModelFactory

@Composable
fun ShopScreen(
    onBackClicked: () -> Unit,
    viewModel: ShopViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val products by viewModel.products.collectAsState()
    val userCoins by viewModel.userCoins.collectAsState()
    
    ShopContent(
        products = products,
        userCoins = userCoins,
        onBackClicked = onBackClicked,
        onBuyClicked = { product, context ->
            viewModel.buyProduct(context as Activity, product)
        }
    )
}

@Composable
fun ShopContent(
    products: List<com.jn.glint.billing.StoreProduct>,
    userCoins: Int,
    onBackClicked: () -> Unit,
    onBuyClicked: (com.jn.glint.billing.StoreProduct, android.content.Context) -> Unit
) {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GlintTopBar(
            title = "Coin Shop",
            onBackClick = onBackClicked,
            coins = userCoins
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (products.isEmpty()) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = NeonCyan)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(products) { product ->
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.surface,
                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                NeonMagenta.copy(alpha = 0.5f)
                            )
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = product.title.uppercase(),
                                        style = MaterialTheme.typography.titleMedium,
                                        color = Color.White
                                    )
                                    Text(
                                        text = product.price,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = CoinGold
                                    )
                                }
                                SmallNeonButton(
                                    text = "BUY",
                                    onClick = {
                                        onBuyClicked(product, context)
                                    },
                                    color = NeonCyan
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShopScreenPreview() {
    GlintTheme {
        ShopContent(
            products = listOf(
                com.jn.glint.billing.StoreProduct("1", "100 Coins", "$0.99", null),
                com.jn.glint.billing.StoreProduct("2", "500 Coins", "$3.99", null),
                com.jn.glint.billing.StoreProduct("3", "1000 Coins", "$6.99", null)
            ),
            userCoins = 250,
            onBackClicked = {},
            onBuyClicked = { _, _ -> }
        )
    }
}
