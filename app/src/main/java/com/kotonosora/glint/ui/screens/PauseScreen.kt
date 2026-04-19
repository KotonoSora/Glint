package com.kotonosora.glint.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotonosora.glint.ui.NeonButton
import com.kotonosora.glint.ui.theme.NeonCyan
import com.kotonosora.glint.ui.theme.NeonMagenta

@Composable
fun PauseScreen(
    onContinueClicked: () -> Unit,
    onQuitClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background.copy(alpha = 0.95f))
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "PAUSED",
            style = MaterialTheme.typography.displayMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(64.dp))

        NeonButton(
            text = "CONTINUE",
            onClick = onContinueClicked,
            color = NeonCyan
        )

        Spacer(modifier = Modifier.height(16.dp))

        NeonButton(
            text = "QUIT",
            onClick = onQuitClicked,
            color = NeonMagenta
        )
    }
}
