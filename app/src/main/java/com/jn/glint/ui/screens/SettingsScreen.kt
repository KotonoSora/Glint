package com.jn.glint.ui.screens

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.jn.glint.ui.GlintTopBar
import com.jn.glint.ui.theme.GlintTheme
import com.jn.glint.ui.theme.NeonCyan
import com.jn.glint.viewmodel.SettingsViewModel
import com.jn.glint.viewmodel.ViewModelFactory

@Composable
fun SettingsScreen(
    onBackClicked: () -> Unit,
    viewModel: SettingsViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val soundEnabled by viewModel.soundEnabled.collectAsState()
    val musicEnabled by viewModel.musicEnabled.collectAsState()

    SettingsContent(
        soundEnabled = soundEnabled,
        musicEnabled = musicEnabled,
        onBackClicked = onBackClicked,
        onSoundToggle = { viewModel.setSoundEnabled(it) },
        onMusicToggle = { viewModel.setMusicEnabled(it) }
    )
}

@Composable
fun SettingsContent(
    soundEnabled: Boolean,
    musicEnabled: Boolean,
    onBackClicked: () -> Unit,
    onSoundToggle: (Boolean) -> Unit,
    onMusicToggle: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        GlintTopBar(
            title = "Settings",
            onBackClick = onBackClicked
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            SettingsToggle(
                label = "SOUND EFFECTS",
                checked = soundEnabled,
                onCheckedChange = onSoundToggle
            )

            Spacer(modifier = Modifier.height(16.dp))

            SettingsToggle(
                label = "BACKGROUND MUSIC",
                checked = musicEnabled,
                onCheckedChange = onMusicToggle
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SettingsScreenPreview() {
    GlintTheme {
        SettingsContent(
            soundEnabled = true,
            musicEnabled = false,
            onBackClicked = {},
            onSoundToggle = {},
            onMusicToggle = {}
        )
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
