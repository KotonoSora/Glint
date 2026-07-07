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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.jn.glint.model.DailyChallengeRules
import com.jn.glint.ui.GlintTopBar
import com.jn.glint.ui.NeonButton
import com.jn.glint.ui.theme.CoinGold
import com.jn.glint.ui.theme.GlintTheme
import com.jn.glint.ui.theme.NeonCyan
import com.jn.glint.ui.theme.NeonMagenta

@Composable
fun DailyChallengeScreen(
    rules: DailyChallengeRules,
    onBackClicked: () -> Unit,
    onPlayClicked: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GlintTopBar(
            title = "Daily Challenge",
            onBackClick = onBackClicked
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "RULES",
                style = MaterialTheme.typography.displaySmall,
                color = NeonCyan,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            RuleItem(text = "• GRID SIZE: ${rules.gridSize}x${rules.gridSize}")
            RuleItem(text = "• MAX MOVES: ${rules.maxMoves}")
            RuleItem(text = "• TIME LIMIT: ${rules.timeLimit}")

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "REWARDS",
                style = MaterialTheme.typography.displaySmall,
                color = NeonMagenta,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "EARN ${rules.rewardMultiplier}X COINS FOR EVERY MATCH!",
                style = MaterialTheme.typography.titleMedium,
                color = CoinGold,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(64.dp))

            NeonButton(
                text = "PLAY WITH RULES",
                onClick = onPlayClicked,
                color = NeonCyan
            )
        }
    }
}

@Composable
fun RuleItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyLarge,
        color = Color.White,
        modifier = Modifier.padding(vertical = 4.dp)
    )
}

@Preview(showBackground = true)
@Composable
fun DailyChallengeScreenPreview() {
    GlintTheme {
        DailyChallengeScreen(
            rules = DailyChallengeRules(),
            onBackClicked = {},
            onPlayClicked = {}
        )
    }
}
