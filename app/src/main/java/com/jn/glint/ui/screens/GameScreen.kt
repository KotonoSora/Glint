package com.jn.glint.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jn.glint.model.GameUiState
import com.jn.glint.model.Tile
import com.jn.glint.model.TileStatus
import com.jn.glint.ui.GlintTopBar
import com.jn.glint.ui.SmallNeonButton
import com.jn.glint.ui.theme.CoinGold
import com.jn.glint.ui.theme.GlintTheme
import com.jn.glint.ui.theme.NeonCyan
import com.jn.glint.ui.theme.NeonGreen
import com.jn.glint.ui.theme.NeonMagenta
import com.jn.glint.viewmodel.GameViewModel
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun GameScreen(
    viewModel: GameViewModel,
    onGameFinished: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    GameContent(
        uiState = uiState,
        onGameFinished = onGameFinished,
        onTileClicked = { index -> viewModel.onTileClicked(index) },
        onUseHint = { viewModel.useHint() },
        onUndoMove = { viewModel.undoMove() }
    )
}

@Composable
fun GameContent(
    uiState: GameUiState,
    onGameFinished: () -> Unit,
    onTileClicked: (Int) -> Unit,
    onUseHint: () -> Unit,
    onUndoMove: () -> Unit
) {
    val haptic = LocalHapticFeedback.current

    LaunchedEffect(uiState.gameCompleted) {
        if (uiState.gameCompleted) {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            delay(1000.milliseconds) // Allow user to see the final match animation
            onGameFinished()
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.background,
        topBar = {
            GlintTopBar(
                title = "Moves: ${uiState.moves}",
                coins = uiState.coins
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Play Area: 4 columns, Square cards (1:1), Scrollable
            LazyVerticalGrid(
                columns = GridCells.Fixed(4),
                modifier = Modifier.weight(1f),
                contentPadding = PaddingValues(2.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(uiState.tiles, key = { _, tile -> tile.id }) { index, tile ->
                    TileItem(
                        tile = tile,
                        onClick = { onTileClicked(index) }
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SmallNeonButton(
                    text = "HINT(50)",
                    onClick = onUseHint,
                    color = NeonCyan,
                    enabled = uiState.coins >= 50 && !uiState.isProcessing
                )
                SmallNeonButton(
                    text = "UNDO(25)",
                    onClick = onUndoMove,
                    color = NeonMagenta,
                    enabled = uiState.coins >= 25 && !uiState.isProcessing
                )
            }
        }
    }
}

@Composable
fun TileItem(
    tile: Tile,
    onClick: () -> Unit
) {
    val isRevealed = tile.status != TileStatus.HIDDEN
    val rotation by animateFloatAsState(
        targetValue = if (isRevealed) 180f else 0f,
        animationSpec = tween(durationMillis = 400)
    )

    Box(
        modifier = Modifier
            .aspectRatio(1f) // Square ratio 1:1
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
                border = BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
                tonalElevation = 2.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        "?",
                        color = NeonCyan,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
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
                    1.5.dp,
                    if (tile.status == TileStatus.MATCHED) NeonGreen else NeonMagenta
                ),
                tonalElevation = 4.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(4.dp)
                ) {
                    // Mass number (top-left) - Science A
                    Text(
                        text = formatScienceNumber(tile.massNumber),
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.9f),
                        modifier = Modifier.align(Alignment.TopStart)
                    )

                    // Symbol (Center)
                    Text(
                        text = tile.symbol,
                        color = if (tile.status == TileStatus.MATCHED) NeonGreen else Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.align(Alignment.Center)
                    )

                    // Atomic number (bottom-left) - Science Z
                    Text(
                        text = formatScienceNumber(tile.atomicNumber),
                        fontSize = 10.sp,
                        color = Color.White.copy(alpha = 0.7f),
                        modifier = Modifier.align(Alignment.BottomStart)
                    )
                }
            }
        }
    }
}

private fun formatScienceNumber(number: Number): String {
    val d = number.toDouble()
    return if (d % 1.0 == 0.0) d.toLong().toString() else "%.2f".format(d)
}

@Preview(name = "6x6 Easy")
@Composable
fun GameScreenEasyPreview() {
    val gridSize = 6
    val sampleTiles = List(gridSize * gridSize) { i ->
        Tile(i, "H", 1, 1, 1, if (i % 3 == 0) TileStatus.REVEALED else TileStatus.HIDDEN)
    }
    val uiState = GameUiState(
        tiles = sampleTiles,
        moves = 5,
        coins = 100,
        gridSize = gridSize
    )
    GlintTheme {
        GameContent(
            uiState = uiState,
            onGameFinished = {},
            onTileClicked = {},
            onUseHint = {},
            onUndoMove = {}
        )
    }
}

@Preview(name = "9x9 Medium")
@Composable
fun GameScreenMediumPreview() {
    val gridSize = 9
    val sampleTiles = List(gridSize * gridSize) { i ->
        Tile(i, "He", 2, 4, 2, if (i % 4 == 0) TileStatus.MATCHED else TileStatus.HIDDEN)
    }
    val uiState = GameUiState(
        tiles = sampleTiles,
        moves = 10,
        coins = 150,
        gridSize = gridSize
    )
    GlintTheme {
        GameContent(
            uiState = uiState,
            onGameFinished = {},
            onTileClicked = {},
            onUseHint = {},
            onUndoMove = {}
        )
    }
}

@Preview(name = "15x15 Hard")
@Composable
fun GameScreenHardPreview() {
    val gridSize = 15
    val sampleTiles = List(gridSize * gridSize) { i ->
        Tile(i, "Li", 3, 7, 3, if (i % 5 == 0) TileStatus.REVEALED else TileStatus.HIDDEN)
    }
    val uiState = GameUiState(
        tiles = sampleTiles,
        moves = 0,
        coins = 200,
        gridSize = gridSize
    )
    GlintTheme {
        GameContent(
            uiState = uiState,
            onGameFinished = {},
            onTileClicked = {},
            onUseHint = {},
            onUndoMove = {}
        )
    }
}
