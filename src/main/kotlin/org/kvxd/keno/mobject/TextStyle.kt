package org.kvxd.keno.mobject

import org.jetbrains.skia.Color
import org.jetbrains.skia.FontStyle

data class TextStyle(
    val fontSize: Double = 0.2,
    val color: Int = Color.WHITE,
    val fontFamily: String? = null,
    val fontStyle: FontStyle = FontStyle.NORMAL,
    val lineHeight: Double = 1.2,
) {
    init {
        require(fontSize > 0.0) { "Font size must be positive" }
        require(lineHeight > 0.0) { "Line height must be positive" }
    }
}
