package org.kvxd.keno.scene

data class Viewport(
    val width: Double,
    val height: Double,
) {
    init {
        require(width.isFinite() && width > 0.0) { "Viewport width must be finite and positive" }
        require(height.isFinite() && height > 0.0) { "Viewport height must be finite and positive" }
    }

    val left: Double
        get() = -width / 2.0

    val right: Double
        get() = width / 2.0

    val bottom: Double
        get() = -height / 2.0

    val top: Double
        get() = height / 2.0

    companion object {
        val Widescreen = Viewport(width = 32.0 / 9.0, height = 2.0)
        val Square = Viewport(width = 2.0, height = 2.0)
    }
}
