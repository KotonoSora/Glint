package com.kotonosora.glint.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.kotonosora.glint.model.CoinRepository
import com.kotonosora.glint.model.GameUiState
import com.kotonosora.glint.model.Tile
import com.kotonosora.glint.model.TileStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class GameViewModel(
    application: Application,
    private val savedStateHandle: SavedStateHandle
) : AndroidViewModel(application) {

    private val coinRepository = CoinRepository(application)
    
    val uiState: StateFlow<GameUiState> = savedStateHandle.getStateFlow("ui_state", GameUiState())

    private val _soundEvent = MutableSharedFlow<String>()
    val soundEvent: SharedFlow<String> = _soundEvent.asSharedFlow()

    private var firstSelectedTileIndex: Int? = null
    private var lastRevealedIndex: Int? = null

    init {
        viewModelScope.launch {
            coinRepository.coinsFlow.collectLatest { coins ->
                updateState { it.copy(coins = coins) }
            }
        }
        if (uiState.value.tiles.isEmpty()) {
            startNewGame(4)
        }
    }

    private fun updateState(function: (GameUiState) -> GameUiState) {
        val newState = function(uiState.value)
        savedStateHandle["ui_state"] = newState
    }

    fun startNewGame(gridSize: Int) {
        val totalTiles = gridSize * gridSize
        val pairs = totalTiles / 2
        val values = (0 until pairs).flatMap { listOf(it, it) }.shuffled()

        val newTiles = values.mapIndexed { index, value ->
            Tile(id = index, value = value)
        }

        updateState {
            GameUiState(
                tiles = newTiles,
                gridSize = gridSize,
                coins = it.coins // Preserve coins across games
            )
        }
        firstSelectedTileIndex = null
    }

    fun onTileClicked(index: Int) {
        val currentState = uiState.value
        if (currentState.isProcessing || currentState.gameCompleted) return

        val clickedTile = currentState.tiles[index]
        if (clickedTile.status != TileStatus.HIDDEN) return

        // Play tap sound
        viewModelScope.launch { _soundEvent.emit("tap") }

        // Reveal the tile
        updateTileStatus(index, TileStatus.REVEALED)
        lastRevealedIndex = index

        if (firstSelectedTileIndex == null) {
            firstSelectedTileIndex = index
        } else {
            val firstIndex = firstSelectedTileIndex!!
            if (firstIndex == index) return

            updateState { it.copy(moves = it.moves + 1, isProcessing = true) }

            viewModelScope.launch {
                delay(1000)
                val firstTile = uiState.value.tiles[firstIndex]
                val secondTile = uiState.value.tiles[index]

                if (firstTile.value == secondTile.value) {
                    // Match found
                    _soundEvent.emit("tap")
                    updateTilesStatus(listOf(firstIndex, index), TileStatus.MATCHED)
                    lastRevealedIndex = null
                    
                    val reward = (uiState.value.matchesFound) * 10 - (uiState.value.moves / 2).coerceAtLeast(0)
                    
                    updateState {
                        val newMatches = it.matchesFound + 1
                        val completed = newMatches == (it.gridSize * it.gridSize) / 2
                        if (completed) {
                            viewModelScope.launch {
                                _soundEvent.emit("win")
                                coinRepository.updateCoins(reward)
                                updateState { state -> state.copy(coins = state.coins + reward) }
                            }
                        }
                        it.copy(
                            matchesFound = newMatches,
                            gameCompleted = completed,
                            isProcessing = false
                        )
                    }
                } else {
                    // Mismatch
                    _soundEvent.emit("error")
                    updateTilesStatus(listOf(firstIndex, index), TileStatus.HIDDEN)
                    lastRevealedIndex = null
                    updateState { it.copy(isProcessing = false) }
                }
                firstSelectedTileIndex = null
            }
        }
    }

    fun undoMove() {
        val currentState = uiState.value
        if (currentState.isProcessing || currentState.gameCompleted || currentState.coins < 25) return

        val lastIdx = lastRevealedIndex ?: return
        if (currentState.tiles[lastIdx].status != TileStatus.REVEALED) return

        viewModelScope.launch {
            updateState { it.copy(coins = it.coins - 25) }
            coinRepository.updateCoins(-25)
            updateTileStatus(lastIdx, TileStatus.HIDDEN)
            firstSelectedTileIndex = null
            lastRevealedIndex = null
            _soundEvent.emit("tap")
        }
    }

    private fun updateTileStatus(index: Int, status: TileStatus) {
        updateState { state ->
            val newTiles = state.tiles.toMutableList()
            newTiles[index] = newTiles[index].copy(status = status)
            state.copy(tiles = newTiles)
        }
    }

    private fun updateTilesStatus(indices: List<Int>, status: TileStatus) {
        updateState { state ->
            val newTiles = state.tiles.toMutableList()
            indices.forEach { index ->
                newTiles[index] = newTiles[index].copy(status = status)
            }
            state.copy(tiles = newTiles)
        }
    }

    fun useHint() {
        val currentState = uiState.value
        if (currentState.isProcessing || currentState.gameCompleted || currentState.coins < 50) return

        // Find the first hidden tile
        val firstHiddenIndex = currentState.tiles.indexOfFirst { it.status == TileStatus.HIDDEN }
        if (firstHiddenIndex == -1) return

        // Find its pair
        val targetValue = currentState.tiles[firstHiddenIndex].value
        val pairIndex = currentState.tiles.withIndex().find {
            it.index != firstHiddenIndex && it.value.value == targetValue
        }?.index ?: return

        viewModelScope.launch {
            updateState { it.copy(isProcessing = true, coins = it.coins - 50) }
            // Deduct coins in repository too
            coinRepository.updateCoins(-50)

            // Briefly reveal
            updateTilesStatus(listOf(firstHiddenIndex, pairIndex), TileStatus.REVEALED)
            _soundEvent.emit("tap")

            delay(1500)

            // Hide again if they haven't been matched (though they shouldn't be matched yet because of isProcessing)
            updateTilesStatus(listOf(firstHiddenIndex, pairIndex), TileStatus.HIDDEN)
            updateState { it.copy(isProcessing = false) }
        }
    }
}
