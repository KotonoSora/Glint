package com.jn.glint.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.glint.domain.usecase.GetUserCoinsUseCase
import com.jn.glint.domain.usecase.UpdateUserCoinsUseCase
import com.jn.glint.model.GameUiState
import com.jn.glint.model.Tile
import com.jn.glint.model.TileStatus
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameViewModel(
    private val getUserCoinsUseCase: GetUserCoinsUseCase,
    private val updateUserCoinsUseCase: UpdateUserCoinsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private val _soundEvent = MutableSharedFlow<String>()
    val soundEvent: SharedFlow<String> = _soundEvent.asSharedFlow()

    private var firstSelectedTileIndex: Int? = null
    private var lastRevealedIndex: Int? = null

    init {
        viewModelScope.launch {
            getUserCoinsUseCase().collectLatest { coins ->
                _uiState.update { it.copy(coins = coins) }
            }
        }
        if (uiState.value.tiles.isEmpty()) {
            startNewGame(4)
        }
    }

    fun startNewGame(gridSize: Int) {
        val totalTiles = gridSize * gridSize
        val pairs = totalTiles / 2
        val values = (0 until pairs).flatMap { listOf(it, it) }.shuffled()

        val newTiles = values.mapIndexed { index, value ->
            Tile(id = index, value = value)
        }

        _uiState.update {
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

            _uiState.update { it.copy(moves = it.moves + 1, isProcessing = true) }

            viewModelScope.launch {
                delay(1000)
                val firstTile = uiState.value.tiles[firstIndex]
                val secondTile = uiState.value.tiles[index]

                if (firstTile.value == secondTile.value) {
                    // Match found
                    _soundEvent.emit("tap")
                    updateTilesStatus(listOf(firstIndex, index), TileStatus.MATCHED)
                    lastRevealedIndex = null

                    val reward =
                        (uiState.value.matchesFound) * 10 - (uiState.value.moves / 2).coerceAtLeast(
                            0
                        )

                    _uiState.update {
                        val newMatches = it.matchesFound + 1
                        val completed = newMatches == (it.gridSize * it.gridSize) / 2
                        if (completed) {
                            viewModelScope.launch {
                                _soundEvent.emit("win")
                                updateUserCoinsUseCase(reward)
                                _uiState.update { state -> state.copy(coins = state.coins + reward) }
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
                    _uiState.update { it.copy(isProcessing = false) }
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
            _uiState.update { it.copy(coins = it.coins - 25) }
            updateUserCoinsUseCase(-25)
            updateTileStatus(lastIdx, TileStatus.HIDDEN)
            firstSelectedTileIndex = null
            lastRevealedIndex = null
            _soundEvent.emit("tap")
        }
    }

    private fun updateTileStatus(index: Int, status: TileStatus) {
        _uiState.update { state ->
            val newTiles = state.tiles.toMutableList()
            newTiles[index] = newTiles[index].copy(status = status)
            state.copy(tiles = newTiles)
        }
    }

    private fun updateTilesStatus(indices: List<Int>, status: TileStatus) {
        _uiState.update { state ->
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
            _uiState.update { it.copy(isProcessing = true, coins = it.coins - 50) }
            // Deduct coins in repository too
            updateUserCoinsUseCase(-50)

            // Briefly reveal
            updateTilesStatus(listOf(firstHiddenIndex, pairIndex), TileStatus.REVEALED)
            _soundEvent.emit("tap")

            delay(1500)

            // Hide again if they haven't been matched (though they shouldn't be matched yet because of isProcessing)
            updateTilesStatus(listOf(firstHiddenIndex, pairIndex), TileStatus.HIDDEN)
            _uiState.update { it.copy(isProcessing = false) }
        }
    }
}
