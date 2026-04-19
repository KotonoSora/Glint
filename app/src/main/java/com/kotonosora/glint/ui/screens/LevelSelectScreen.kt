package com.kotonosora.glint.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.kotonosora.glint.ui.NeonButton
import com.kotonosora.glint.ui.theme.NeonCyan
import com.kotonosora.glint.ui.theme.NeonMagenta

@Composable
fun LevelSelectScreen(
    onLevelSelected: (Int) -> Unit,
    onBackClicked: () -> Unit
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
            text = "SELECT LEVEL",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(64.dp))

        NeonButton(text = "2x2 EASY", onClick = { onLevelSelected(2) }, color = NeonCyan)
        Spacer(modifier = Modifier.height(16.dp))
        NeonButton(text = "4x4 NORMAL", onClick = { onLevelSelected(4) }, color = NeonMagenta)
        Spacer(modifier = Modifier.height(16.dp))
        NeonButton(text = "6x6 HARD", onClick = { onLevelSelected(6) }, color = NeonCyan)

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
