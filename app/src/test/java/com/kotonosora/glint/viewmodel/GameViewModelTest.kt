package com.kotonosora.glint.viewmodel

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.kotonosora.glint.model.TileStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@OptIn(ExperimentalCoroutinesApi)
@RunWith(RobolectricTestRunner::class)
class GameViewModelTest {

    private lateinit var viewModel: GameViewModel
    private val testDispatcher = StandardTestDispatcher()
    private val application = ApplicationProvider.getApplicationContext<Application>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = GameViewModel(application)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `startNewGame initializes grid correctly`() = runTest {
        viewModel.startNewGame(4)
        val state = viewModel.uiState.value
        assertEquals(16, state.tiles.size)
        assertEquals(4, state.gridSize)
        assertEquals(0, state.moves)
        assertEquals(0, state.matchesFound)
        assertTrue(state.tiles.all { it.status == TileStatus.HIDDEN })
    }

    @Test
    fun `onTileClicked reveals tile`() = runTest {
        viewModel.startNewGame(2) // 4 tiles
        viewModel.onTileClicked(0)

        assertEquals(TileStatus.REVEALED, viewModel.uiState.value.tiles[0].status)
    }

    @Test
    fun `matching tiles stay revealed and increment matches`() = runTest {
        viewModel.startNewGame(2)
        val tiles = viewModel.uiState.value.tiles
        val firstIndex = 0
        val firstValue = tiles[firstIndex].value
        val secondIndex = tiles.indexOfLast { it.value == firstValue }

        viewModel.onTileClicked(firstIndex)
        viewModel.onTileClicked(secondIndex)

        // Advance time for the match delay
        advanceTimeBy(1100)

        val state = viewModel.uiState.value
        assertEquals(TileStatus.MATCHED, state.tiles[firstIndex].status)
        assertEquals(TileStatus.MATCHED, state.tiles[secondIndex].status)
        assertEquals(1, state.matchesFound)
    }

    @Test
    fun `mismatched tiles hide after delay`() = runTest {
        viewModel.startNewGame(2)
        val tiles = viewModel.uiState.value.tiles
        val firstIndex = 0
        val firstValue = tiles[firstIndex].value
        val secondIndex = tiles.indexOfFirst { it.value != firstValue }

        viewModel.onTileClicked(firstIndex)
        viewModel.onTileClicked(secondIndex)

        assertEquals(true, viewModel.uiState.value.isProcessing)

        // Advance time for the mismatch delay
        advanceTimeBy(1100)

        val state = viewModel.uiState.value
        assertEquals(TileStatus.HIDDEN, state.tiles[firstIndex].status)
        assertEquals(TileStatus.HIDDEN, state.tiles[secondIndex].status)
        assertEquals(false, state.isProcessing)
        assertEquals(1, state.moves)
    }

    @Test
    fun `game completion detected`() = runTest {
        viewModel.startNewGame(2) // 2 pairs
        val tiles = viewModel.uiState.value.tiles

        // Match first pair
        val val1 = tiles[0].value
        val pair1 = tiles.withIndex().filter { it.value.value == val1 }.map { it.index }
        viewModel.onTileClicked(pair1[0])
        viewModel.onTileClicked(pair1[1])
        advanceTimeBy(1100)

        // Match second pair
        val val2 = tiles.first { it.value != val1 }.value
        val pair2 = tiles.withIndex().filter { it.value.value == val2 }.map { it.index }
        viewModel.onTileClicked(pair2[0])
        viewModel.onTileClicked(pair2[1])
        advanceTimeBy(1100)

        assertTrue(viewModel.uiState.value.gameCompleted)
    }
}
