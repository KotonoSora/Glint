package com.jn.glint.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.jn.glint.ui.screens.DailyChallengeScreen
import com.jn.glint.ui.screens.GameScreen
import com.jn.glint.ui.screens.HelpScreen
import com.jn.glint.ui.screens.HomeScreen
import com.jn.glint.ui.screens.LeaderboardScreen
import com.jn.glint.ui.screens.LevelSelectScreen
import com.jn.glint.ui.screens.ResultScreen
import com.jn.glint.ui.screens.SettingsScreen
import com.jn.glint.ui.screens.ShopScreen
import com.jn.glint.viewmodel.GameViewModel

@Composable
fun AppNavigation(gameViewModel: GameViewModel) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            val uiState by gameViewModel.uiState.collectAsState()
            HomeScreen(
                coins = uiState.coins,
                onPlayClicked = { navController.navigate("level_select") },
                onDailyChallengeClicked = { navController.navigate("daily_challenge") },
                onLeaderboardClicked = { navController.navigate("leaderboard") },
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
                onGameFinished = { navController.navigate("result") },
                onQuickBuyClicked = { navController.navigate("shop") }
            )
        }
        composable("daily_challenge") {
            val uiState by gameViewModel.uiState.collectAsState()
            DailyChallengeScreen(
                rules = uiState.dailyChallengeRules,
                onBackClicked = { navController.popBackStack() },
                onPlayClicked = {
                    val rules = uiState.dailyChallengeRules
                    gameViewModel.startNewGame(
                        gridSize = rules.gridSize,
                        maxMoves = rules.maxMoves,
                        isDailyChallenge = true
                    )
                    navController.navigate("game")
                }
            )
        }
        composable("leaderboard") {
            val uiState by gameViewModel.uiState.collectAsState()
            LeaderboardScreen(
                entries = uiState.historyEntries,
                onBackClicked = { navController.popBackStack() }
            )
        }
        composable("result") {
            ResultScreen(
                viewModel = gameViewModel,
                onPlayAgainClicked = {
                    val state = gameViewModel.uiState.value
                    gameViewModel.startNewGame(
                        gridSize = state.gridSize,
                        maxMoves = state.maxMoves,
                        isDailyChallenge = state.isDailyChallenge
                    )
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
