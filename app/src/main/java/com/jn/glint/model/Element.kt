package com.jn.glint.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Represents a chemical element.
 */
@Parcelize
data class Element(
    val symbol: String,
    val atomicNumber: Int
) : Parcelable
