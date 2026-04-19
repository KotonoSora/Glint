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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.kotonosora.glint.ui.NeonButton
import com.kotonosora.glint.ui.theme.CoinGold
import com.kotonosora.glint.ui.theme.NeonCyan
import com.kotonosora.glint.ui.theme.NeonMagenta
import com.kotonosora.glint.viewmodel.GameViewModel

@Composable
fun ResultScreen(
    viewModel: GameViewModel,
    onPlayAgainClicked: () -> Unit,
    onHomeClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val earnedCoins = (uiState.matchesFound * 10) - (uiState.moves / 2).coerceAtLeast(0)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "VICTORY!",
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "TOTAL MOVES: ${uiState.moves}",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "COINS EARNED: $earnedCoins",
            style = MaterialTheme.typography.titleMedium,
            color = CoinGold
        )

        Spacer(modifier = Modifier.height(64.dp))

        NeonButton(
            text = "PLAY AGAIN",
            onClick = onPlayAgainClicked,
            color = NeonCyan
        )

        Spacer(modifier = Modifier.height(16.dp))

        NeonButton(
            text = "HOME",
            onClick = onHomeClicked,
            color = NeonMagenta
        )
    }
}
