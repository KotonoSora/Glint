package com.kotonosora.glint.ui.screens

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kotonosora.glint.ui.SmallNeonButton
import com.kotonosora.glint.ui.theme.CoinGold
import com.kotonosora.glint.ui.theme.NeonCyan
import com.kotonosora.glint.ui.theme.NeonMagenta
import com.kotonosora.glint.viewmodel.ShopViewModel

@Composable
fun ShopScreen(
    onBackClicked: () -> Unit,
    viewModel: ShopViewModel = viewModel()
) {
    val products by viewModel.products.collectAsState()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "COIN SHOP",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

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
                                    viewModel.buyProduct(context as Activity, product)
                                },
                                color = NeonCyan
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        TextButton(onClick = onBackClicked) {
            Text(
                "BACK",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}
