package com.jn.glint.domain.usecase

import com.jn.glint.domain.repository.GameRepository
import com.jn.glint.model.Tile

/**
 * Use case to generate a list of tiles for a new game.
 */
class GenerateTilesUseCase(private val repository: GameRepository) {
    operator fun invoke(gridSize: Int): List<Tile> {
        val elements = repository.getElements()
        val totalTiles = gridSize * gridSize
        val numPairs = totalTiles / 2

        // Create enough pairs by selecting random elements and giving them random variations
        val tileData = List(numPairs) {
            val element = elements.random()
            // Generate a random isotope (mass number approx 2x atomic number)
            val massNumber = element.atomicNumber * 2 + (-1..2).random()
            // Generate random electron count (neutral or +/- 1)
            val electrons = element.atomicNumber + (-1..1).random()
            element to (massNumber to electrons)
        }

        val values = tileData.flatMap { (element, variations) ->
            val (mass, elecs) = variations
            listOf(
                Triple(element, mass, elecs),
                Triple(element, mass, elecs)
            )
        }.shuffled()

        return values.mapIndexed { index, (element, mass, elecs) ->
            Tile(
                id = index,
                symbol = element.symbol,
                atomicNumber = element.atomicNumber,
                massNumber = mass,
                electrons = elecs
            )
        }
    }
}
