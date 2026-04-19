package com.kotonosora.glint.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kotonosora.glint.ui.theme.NeonCyan
import com.kotonosora.glint.viewmodel.SettingsViewModel

@Composable
fun SettingsScreen(
    onBackClicked: () -> Unit,
    viewModel: SettingsViewModel = viewModel()
) {
    val soundEnabled by viewModel.soundEnabled.collectAsState()
    val musicEnabled by viewModel.musicEnabled.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "SETTINGS",
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(64.dp))

        SettingsToggle(
            label = "SOUND EFFECTS",
            checked = soundEnabled,
            onCheckedChange = { viewModel.setSoundEnabled(it) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        SettingsToggle(
            label = "BACKGROUND MUSIC",
            checked = musicEnabled,
            onCheckedChange = { viewModel.setMusicEnabled(it) }
        )

        Spacer(modifier = Modifier.height(64.dp))

        TextButton(onClick = onBackClicked) {
            Text(
                "BACK",
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}

@Composable
fun SettingsToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            color = Color.White
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = NeonCyan,
                checkedTrackColor = NeonCyan.copy(alpha = 0.5f),
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = Color.DarkGray
            )
        )
    }
}
