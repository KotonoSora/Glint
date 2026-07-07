package com.jn.glint.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jn.glint.ui.GlintTopBar
import com.jn.glint.ui.NeonButton
import com.jn.glint.ui.theme.AppTitleColor
import com.jn.glint.ui.theme.GlintTheme
import com.jn.glint.ui.theme.HelpButtonColor
import com.jn.glint.ui.theme.PlayButtonColor
import com.jn.glint.ui.theme.SettingsButtonColor
import com.jn.glint.ui.theme.ShopButtonColor

@Composable
fun HomeScreen(
    coins: Int,
    onPlayClicked: () -> Unit,
    onShopClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onHelpClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        topBar = {
            GlintTopBar(
                title = "",
                coins = coins,
                onShopClicked = onShopClicked
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "GLINT",
                style = MaterialTheme.typography.displayLarge,
                color = AppTitleColor
            )

            Spacer(modifier = Modifier.height(64.dp))

            NeonButton(text = "PLAY", onClick = onPlayClicked, color = PlayButtonColor)
            Spacer(modifier = Modifier.height(16.dp))
            NeonButton(
                text = "SETTINGS",
                onClick = onSettingsClicked,
                color = SettingsButtonColor
            )
            Spacer(modifier = Modifier.height(16.dp))
            NeonButton(text = "HELP", onClick = onHelpClicked, color = HelpButtonColor)
        }

    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    GlintTheme {
        HomeScreen(
            coins = 100,
            onPlayClicked = {},
            onShopClicked = {},
            onSettingsClicked = {},
            onHelpClicked = {}
        )
    }
}
