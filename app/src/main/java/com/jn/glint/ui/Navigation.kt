package com.jn.glint.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jn.glint.ui.screens.GameScreen
import com.jn.glint.ui.screens.HelpScreen
import com.jn.glint.ui.screens.HomeScreen
import com.jn.glint.ui.screens.LevelSelectScreen
import com.jn.glint.ui.screens.PauseScreen
import com.jn.glint.ui.screens.ResultScreen
import com.jn.glint.ui.screens.SettingsScreen
import com.jn.glint.ui.screens.ShopScreen
import com.jn.glint.viewmodel.GameViewModel

@Composable
fun AppNavigation(gameViewModel: GameViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                onPlayClicked = { navController.navigate("level_select") },
                onShopClicked = { navController.navigate("shop") },
                onSettingsClicked = { navController.navigate("settings") },
                onHelpClicked = { navController.navigate("help") }
            )
        }
        composable("level_select") {
            LevelSelectScreen(
                onLevelSelected = { size ->
                    gameViewModel.startNewGame(size)
                    navController.navigate("game")
                },
                onBackClicked = { navController.popBackStack() }
            )
        }
        composable("game") {
            GameScreen(
                viewModel = gameViewModel,
                onGameFinished = { navController.navigate("result") }
            )
        }
        composable("pause") {
            PauseScreen(
                onContinueClicked = { navController.popBackStack() },
                onQuitClicked = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable("result") {
            ResultScreen(
                viewModel = gameViewModel,
                onPlayAgainClicked = {
                    gameViewModel.startNewGame(gameViewModel.uiState.value.gridSize)
                    navController.navigate("game") {
                        popUpTo("game") { inclusive = true }
                    }
                },
                onHomeClicked = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable("shop") {
            ShopScreen(onBackClicked = { navController.popBackStack() })
        }
        composable("settings") {
            SettingsScreen(onBackClicked = { navController.popBackStack() })
        }
        composable("help") {
            HelpScreen(onBackClicked = { navController.popBackStack() })
        }
    }
}
