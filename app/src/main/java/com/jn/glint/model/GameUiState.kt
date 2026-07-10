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
    val isGameOver: Boolean = false,
    val coins: Int = 0,
    val gridSize: Int = 4,
    val maxMoves: Int = 0,
    val isDailyChallenge: Boolean = false,
    val dailyChallengeRules: DailyChallengeRules = DailyChallengeRules(),
    val historyEntries: List<HistoryEntry> = emptyList()
) : Parcelable

@Parcelize
data class DailyChallengeRules(
    val gridSize: Int = 4,
    val maxMoves: Int = 50,
    val timeLimit: String = "NONE",
    val rewardMultiplier: Int = 2
) : Parcelable

@Parcelize
data class HistoryEntry(
    val dateTime: String,
    val score: Int,
    val reward: Int
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
