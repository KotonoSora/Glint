package com.kotonosora.glint.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HelpScreen(onBackClicked: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "HOW TO PLAY",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.padding(top = 48.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        HelpItem("1. TAP TILES TO FLIP THEM REVEALING THEIR VALUES.")
        HelpItem("2. MATCH TWO TILES WITH THE SAME VALUE TO CLEAR THEM.")
        HelpItem("3. MISMATCHED TILES WILL HIDE AFTER A SHORT DELAY.")
        HelpItem("4. CLEAR ALL TILES TO WIN AND EARN COINS!")
        HelpItem("5. USE HINTS AND UNDO MOVES TO HELP YOU PROGRESS.")

        Spacer(modifier = Modifier.weight(1f))

        TextButton(onClick = onBackClicked) {
            Text(
                "BACK",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun HelpItem(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = Color.White,
        textAlign = TextAlign.Center,
        modifier = Modifier.padding(vertical = 12.dp)
    )
}
