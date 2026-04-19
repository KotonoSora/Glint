package com.kotonosora.glint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.kotonosora.glint.ui.AppNavigation
import com.kotonosora.glint.ui.SoundManager
import com.kotonosora.glint.ui.theme.GlintTheme
import com.kotonosora.glint.viewmodel.GameViewModel
import com.kotonosora.glint.viewmodel.SettingsViewModel
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val gameViewModel: GameViewModel by viewModels()
            val settingsViewModel: SettingsViewModel by viewModels()
            val context = LocalContext.current
            val soundManager = remember { SoundManager(context) }
            val soundEnabled by settingsViewModel.soundEnabled.collectAsState()

            LaunchedEffect(soundEnabled) {
                gameViewModel.soundEvent.collectLatest { soundName ->
                    if (soundEnabled) {
                        soundManager.playSound(soundName)
                    }
                }
            }

            DisposableEffect(Unit) {
                onDispose {
                    soundManager.release()
                }
            }

            GlintTheme {
                AppNavigation(gameViewModel = gameViewModel)
            }
        }
    }
}
