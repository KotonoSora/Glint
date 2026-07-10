package com.jn.glint.ui.screens

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jn.glint.ui.NeonButton
import com.jn.glint.ui.theme.CoinGold
import com.jn.glint.ui.theme.GlintTheme
import com.jn.glint.ui.theme.NeonCyan
import com.jn.glint.ui.theme.NeonMagenta
import com.jn.glint.viewmodel.GameViewModel

@Composable
fun ResultScreen(
    viewModel: GameViewModel,
    onPlayAgainClicked: () -> Unit,
    onHomeClicked: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val isVictory = uiState.gameCompleted
    val earnedCoins = if (isVictory) {
        (uiState.matchesFound * 10) - (uiState.moves / 2).coerceAtLeast(0)
    } else 0

    ResultContent(
        isVictory = isVictory,
        moves = uiState.moves,
        earnedCoins = earnedCoins,
        onPlayAgainClicked = onPlayAgainClicked,
        onHomeClicked = onHomeClicked
    )
}

@Composable
fun ResultContent(
    isVictory: Boolean,
    moves: Int,
    earnedCoins: Int,
    onPlayAgainClicked: () -> Unit,
    onHomeClicked: () -> Unit
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
            text = if (isVictory) "VICTORY!" else "GAME OVER",
            style = MaterialTheme.typography.displayMedium,
            color = if (isVictory) MaterialTheme.colorScheme.primary else NeonMagenta
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "TOTAL MOVES: $moves",
            style = MaterialTheme.typography.titleMedium,
            color = Color.White
        )

        if (isVictory) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "COINS EARNED: $earnedCoins",
                style = MaterialTheme.typography.titleMedium,
                color = CoinGold
            )
        }

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

@Preview(showBackground = true, name = "Victory")
@Composable
fun ResultScreenVictoryPreview() {
    GlintTheme {
        ResultContent(
            isVictory = true,
            moves = 42,
            earnedCoins = 350,
            onPlayAgainClicked = {},
            onHomeClicked = {}
        )
    }
}

@Preview(showBackground = true, name = "GameOver")
@Composable
fun ResultScreenGameOverPreview() {
    GlintTheme {
        ResultContent(
            isVictory = false,
            moves = 50,
            earnedCoins = 0,
            onPlayAgainClicked = {},
            onHomeClicked = {}
        )
    }
}
