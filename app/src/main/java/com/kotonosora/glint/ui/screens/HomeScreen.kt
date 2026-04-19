package com.kotonosora.glint.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotonosora.glint.ui.NeonButton
import com.kotonosora.glint.ui.theme.NeonCyan
import com.kotonosora.glint.ui.theme.NeonMagenta

@Composable
fun HomeScreen(
    onPlayClicked: () -> Unit,
    onShopClicked: () -> Unit,
    onSettingsClicked: () -> Unit,
    onHelpClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "GLINT",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(64.dp))

        NeonButton(text = "PLAY", onClick = onPlayClicked, color = NeonCyan)
        Spacer(modifier = Modifier.height(16.dp))
        NeonButton(text = "SHOP", onClick = onShopClicked, color = NeonMagenta)
        Spacer(modifier = Modifier.height(16.dp))
        NeonButton(text = "SETTINGS", onClick = onSettingsClicked, color = NeonCyan)
        Spacer(modifier = Modifier.height(16.dp))
        NeonButton(text = "HELP", onClick = onHelpClicked, color = NeonMagenta)
    }
}
