package com.jn.glint.domain.repository

import com.jn.glint.model.Element

/**
 * Interface for game-related data operations.
 */
interface GameRepository {
    /**
     * Returns a list of all available chemical elements.
     */
    fun getElements(): List<Element>
}
