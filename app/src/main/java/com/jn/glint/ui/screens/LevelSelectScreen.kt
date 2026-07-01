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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.jn.glint.ui.NeonButton
import com.jn.glint.ui.theme.NeonCyan
import com.jn.glint.ui.theme.NeonMagenta

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

        NeonButton(text = "6x6 EASY", onClick = { onLevelSelected(6) }, color = NeonCyan)
        Spacer(modifier = Modifier.height(16.dp))
        NeonButton(text = "9x9 MEDIUM", onClick = { onLevelSelected(9) }, color = NeonMagenta)
        Spacer(modifier = Modifier.height(16.dp))
        NeonButton(text = "15x15 HARD", onClick = { onLevelSelected(15) }, color = NeonCyan)

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
