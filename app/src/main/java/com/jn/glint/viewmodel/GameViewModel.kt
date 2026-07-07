package com.jn.glint.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.glint.domain.usecase.GenerateTilesUseCase
import com.jn.glint.domain.usecase.GetUserCoinsUseCase
import com.jn.glint.domain.usecase.UpdateUserCoinsUseCase
import com.jn.glint.model.GameUiState
import com.jn.glint.model.HistoryEntry
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
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.time.Duration.Companion.milliseconds

class GameViewModel(
    private val getUserCoinsUseCase: GetUserCoinsUseCase,
    private val updateUserCoinsUseCase: UpdateUserCoinsUseCase,
    private val generateTilesUseCase: GenerateTilesUseCase
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
            startNewGame(6)
        }
    }

    fun startNewGame(gridSize: Int, maxMoves: Int = 0, isDailyChallenge: Boolean = false) {
        val newTiles = generateTilesUseCase(gridSize)

        _uiState.update { state ->
            state.copy(
                tiles = newTiles,
                gridSize = gridSize,
                matchesFound = 0,
                moves = 0,
                gameCompleted = false,
                isGameOver = false,
                isProcessing = false,
                maxMoves = maxMoves,
                isDailyChallenge = isDailyChallenge
            )
        }
        firstSelectedTileIndex = null
        lastRevealedIndex = null
    }

    fun onTileClicked(index: Int) {
        val currentState = uiState.value
        if (currentState.isProcessing || currentState.gameCompleted || currentState.isGameOver) return

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
                delay(1000.milliseconds)
                val firstTile = uiState.value.tiles[firstIndex]
                val secondTile = uiState.value.tiles[index]

                if (firstTile.value == secondTile.value) {
                    // Match found
                    viewModelScope.launch { _soundEvent.emit("tap") }

                    _uiState.update { state ->
                        val newTiles = state.tiles.toMutableList()
                        newTiles[firstIndex] =
                            newTiles[firstIndex].copy(status = TileStatus.MATCHED)
                        newTiles[index] = newTiles[index].copy(status = TileStatus.MATCHED)

                        val newMatches = state.matchesFound + 1
                        val completed = newMatches == state.tiles.size / 2

                        val baseReward = newMatches * 10 - (state.moves / 2).coerceAtLeast(0)
                        val reward = if (state.isDailyChallenge) {
                            baseReward * state.dailyChallengeRules.rewardMultiplier
                        } else {
                            baseReward
                        }.coerceAtLeast(0)

                        if (completed) {
                            viewModelScope.launch {
                                delay(500.milliseconds) // Small delay to let user see the last match
                                _soundEvent.emit("win")
                                updateUserCoinsUseCase(reward)

                                val currentDateTime = LocalDateTime.now().format(
                                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                                )
                                val newEntry = HistoryEntry(
                                    dateTime = currentDateTime,
                                    score = (10000 - state.moves * 100).coerceAtLeast(0), // Example score logic
                                    reward = reward
                                )

                                _uiState.update { state ->
                                    state.copy(
                                        coins = state.coins + reward,
                                        historyEntries = (listOf(newEntry) + state.historyEntries).take(
                                            10
                                        )
                                    )
                                }
                            }
                        }

                        state.copy(
                            tiles = newTiles,
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

                    _uiState.update { state ->
                        val gameOver = state.maxMoves > 0 && state.moves >= state.maxMoves
                        if (gameOver) {
                            viewModelScope.launch { _soundEvent.emit("error") }
                        }
                        state.copy(isProcessing = false, isGameOver = gameOver)
                    }
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

            delay(1500.milliseconds)

            // Hide again if they haven't been matched (though they shouldn't be matched yet because of isProcessing)
            updateTilesStatus(listOf(firstHiddenIndex, pairIndex), TileStatus.HIDDEN)
            _uiState.update { it.copy(isProcessing = false) }
        }
    }
}
