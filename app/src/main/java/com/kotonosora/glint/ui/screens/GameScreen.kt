package com.kotonosora.glint.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import com.kotonosora.glint.model.Tile
import com.kotonosora.glint.model.TileStatus
import com.kotonosora.glint.ui.SmallNeonButton
import com.kotonosora.glint.ui.theme.CoinGold
import com.kotonosora.glint.ui.theme.NeonCyan
import com.kotonosora.glint.ui.theme.NeonGreen
import com.kotonosora.glint.ui.theme.NeonMagenta
import com.kotonosora.glint.viewmodel.GameViewModel

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onPauseClicked: () -> Unit,
    onGameFinished: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(uiState.gameCompleted) {
        if (uiState.gameCompleted) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onGameFinished()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "MOVES: ${uiState.moves}",
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White
                )
                Text(
                    text = "COINS: ${uiState.coins}",
                    style = MaterialTheme.typography.titleSmall,
                    color = CoinGold
                )
            }
            IconButton(onClick = onPauseClicked) {
                Text(
                    "PAUSE",
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(uiState.gridSize),
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(4.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(uiState.tiles, key = { _, tile -> tile.id }) { index, tile ->
                TileItem(tile = tile, onClick = { viewModel.onTileClicked(index) })
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            SmallNeonButton(
                text = "HINT(50)",
                onClick = { viewModel.useHint() },
                color = NeonCyan,
                enabled = uiState.coins >= 50 && !uiState.isProcessing
            )
            SmallNeonButton(
                text = "UNDO(25)",
                onClick = { viewModel.undoMove() },
                color = NeonMagenta,
                enabled = uiState.coins >= 25 && !uiState.isProcessing
            )
        }
    }
}

@Composable
fun TileItem(tile: Tile, onClick: () -> Unit) {
    val isRevealed = tile.status != TileStatus.HIDDEN
    val rotation by animateFloatAsState(
        targetValue = if (isRevealed) 180f else 0f,
        animationSpec = tween(durationMillis = 400)
    )

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .graphicsLayer {
                rotationY = rotation
                cameraDistance = 12f * density
            }
            .clickable(enabled = tile.status == TileStatus.HIDDEN) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        if (rotation <= 90f) {
            // Back of the card (Hidden)
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(2.dp, NeonCyan.copy(alpha = 0.5f)),
                tonalElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "?",
                        color = NeonCyan,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        } else {
            // Front of the card (Revealed/Matched)
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer { rotationY = 180f },
                shape = RoundedCornerShape(8.dp),
                color = MaterialTheme.colorScheme.surfaceVariant,
                border = BorderStroke(
                    2.dp,
                    if (tile.status == TileStatus.MATCHED) NeonGreen else NeonMagenta
                ),
                tonalElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = tile.value.toString(),
                        color = if (tile.status == TileStatus.MATCHED) NeonGreen else Color.White,
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
}
