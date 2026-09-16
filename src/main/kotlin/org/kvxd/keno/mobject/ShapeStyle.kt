package org.kvxd.keno.mobject

import org.jetbrains.skia.Color

data class ShapeStyle(
    val fillColor: Int? = null,
    val strokeColor: Int = Color.WHITE,
    val strokeWidth: Double = 0.01,
) {
    init {
        require(strokeWidth >= 0.0) { "Stroke width cannot be negative" }
        require(fillColor != null || strokeWidth > 0.0) { "A shape needs a fill or a visible stroke" }
    }
}
