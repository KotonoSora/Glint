package com.jn.glint.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class GameUiState(
    val tiles: List<Tile> = emptyList(),
    val moves: Int = 0,
    val matchesFound: Int = 0,
    val isProcessing: Boolean = false,
    val gameCompleted: Boolean = false,
    val coins: Int = 0,
    val gridSize: Int = 4 // e.g., 4 for a 4x4 grid
) : Parcelable

@Parcelize
data class Tile(
    val id: Int,
    val symbol: String,
    val atomicNumber: Int,
    val massNumber: Int,
    val electrons: Int,
    val status: TileStatus = TileStatus.HIDDEN
) : Parcelable {
    // Helper to get a unique value for matching
    val value: String get() = "$symbol-$massNumber-$electrons"
}

enum class TileStatus {
    HIDDEN, REVEALED, MATCHED
}
