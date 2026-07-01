package com.jn.glint

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.jn.glint.ui.AppNavigation
import com.jn.glint.ui.theme.GlintTheme
import com.jn.glint.viewmodel.GameViewModel
import com.jn.glint.viewmodel.SettingsViewModel
import com.jn.glint.viewmodel.ViewModelFactory
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT),
            navigationBarStyle = SystemBarStyle.dark(android.graphics.Color.TRANSPARENT)
        )
        setContent {
            val gameViewModel: GameViewModel by viewModels { ViewModelFactory.Factory }
            val settingsViewModel: SettingsViewModel by viewModels { ViewModelFactory.Factory }

            val container = (application as GlintApplication).container
            val soundManager = container.soundManager
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
                    // soundManager is managed by AppContainer now, 
                    // but we might still want to release it when the app is destroyed
                    // or let the AppContainer handle it. 
                    // For now, keeping it consistent with previous logic if needed.
                }
            }

            GlintTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        AppNavigation(gameViewModel = gameViewModel)
                    }
                }
            }
        }
    }
}
