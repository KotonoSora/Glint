# Tile Flip Match Lite - Implementation Plan

## 1. Architecture Overview (MVVM + Jetpack Compose)
- **View**: Jetpack Compose for declarative UI. Screens are Composable functions observing StateFlow from ViewModels.
- **ViewModel**: Manages UI state, handles user interactions, and orchestrates game logic.
- **Model**: Data classes for Tile, GameState, and UserProfile.
- **Repository**: Handles data persistence (Room/DataStore) and IAP (Google Play Billing).
- **Navigation**: Compose Navigation Component for seamless screen transitions.

## 2. Core Game Logic & Data Models
### Data Models
```kotlin
enum class TileStatus { HIDDEN, REVEALED, MATCHED }

data class Tile(
    val id: Int,
    val value: Int, // Pair identifier
    val status: TileStatus = TileStatus.HIDDEN
)

data class GameUiState(
    val tiles: List<Tile> = emptyList(),
    val moves: Int = 0,
    val matchesFound: Int = 0,
    val isProcessing: Boolean = false,
    val gameCompleted: Boolean = false,
    val coins: Int = 0
)
```
### Game Loop
1. **Shuffle**: Generate pairs, shuffle, and assign to grid.
2. **Flip**: User taps tile -> Update status to `REVEALED` (if not already revealed/matched and not processing).
3. **Validation**: If two tiles are `REVEALED`:
    - Set `isProcessing = true` to lock input.
    - Compare values.
    - **Match**: Update both to `MATCHED`, increment `matchesFound`.
    - **Mismatch**: Delay 1s, then reset both to `HIDDEN`.
    - Set `isProcessing = false`.
4. **Completion**: If `matchesFound == totalPairs`, trigger `gameCompleted`.

## 3. UI Screens (8 Total)
1. **Home**: Main menu (Play, Shop, Settings, Help).
2. **Level Select**: Grid size options (2x2, 4x4, 6x6).
3. **Game Play**: The matching grid, move counter, and coin balance.
4. **Pause**: Menu overlay during gameplay.
5. **Result**: Score, coins earned, and navigation back to Home/Replay.
6. **Coin Shop**: IAP for purchasing coin packs.
7. **Settings**: Sound/Music toggles, Clear Data.
8. **Help**: Rules and how to play.

## 4. Coin Economy & IAP
### Coin Sinks
- **Hint (50 coins)**: Briefly reveals one unmatched pair.
- **Undo (20 coins)**: Flips back the last mismatch if the user is quick.
- **Extra Time/Moves**: Depending on game mode.

### IAP Integration (Google Play Billing)
- **Products**: `coins_small`, `coins_medium`, `coins_large`.
- **Flow**: Query products -> Launch billing flow -> Verify purchase (Server-side or local) -> Update balance in `DataStore`.

## 5. Technical Implementation & Edge Cases
- **Recomposition Optimization**: Use `key` in `LazyVerticalGrid` and stable data models.
- **Input Locking**: Prevent tapping more than 2 tiles or tapping while evaluating a mismatch.
- **Configuration Changes**: ViewModel survives rotation; use `SavedStateHandle` for process death.
- **Animations**: `animateFloatAsState` for tile flip (Y-axis rotation).

## 6. Test-Driven Development (TDD) Plan
### Unit Tests (JUnit 5 + MockK)
- **Match Logic**: Verify `checkMatch` returns true for same values and false for different.
- **State Transitions**: Ensure tile status moves correctly (HIDDEN -> REVEALED -> MATCHED).
- **Shuffle Fairness**: Verify random distribution of pairs.
- **ViewModel**: Test move counting and game completion triggers.

### Acceptance Criteria
- [ ] Clicking a hidden tile reveals its value.
- [ ] Matching pairs remain revealed.
- [ ] Non-matching pairs hide after a short delay.
- [ ] Input is locked during the mismatch delay.
- [ ] Game ends when all pairs are matched.

## 7. Step-by-Step TODO Tasks
### Phase 1: Project Setup & Architecture
- [ ] Initialize project with Jetpack Compose.
- [ ] Set up Dependency Injection (Hilt).
- [ ] Implement Navigation scaffold.

### Phase 2: Core Game Module
- [ ] Implement `GameViewModel` with state management.
- [ ] Create shuffle and match logic.
- [ ] Add unit tests for game logic.

### Phase 3: UI Implementation
- [ ] Design and implement Home, Level Select, and Game Play screens.
- [ ] Implement flip animations and grid layout.
- [ ] Build Pause, Result, Settings, and Help screens.

### Phase 4: Coin System & IAP
- [ ] Implement local coin storage (DataStore).
- [ ] Integrate Google Play Billing Library.
- [ ] Create Coin Shop UI and purchase flow.

### Phase 5: Polish & Testing
- [ ] Add sound effects and haptic feedback.
- [ ] Perform UI testing (Compose Test Rule).
- [ ] Handle edge cases (rapid taps, backgrounding).
