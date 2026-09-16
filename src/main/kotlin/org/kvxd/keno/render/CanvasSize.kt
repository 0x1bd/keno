package org.kvxd.keno.render

data class CanvasSize(
    val width: Int,
    val height: Int,
) {
    init {
        require(width > 0) { "Canvas width must be positive" }
        require(height > 0) { "Canvas height must be positive" }
        require(width.toLong() * height * BYTES_PER_PIXEL <= Int.MAX_VALUE) {
            "Canvas is too large for an in-memory RGBA frame"
        }
    }

    val rowBytes: Int
        get() = width * BYTES_PER_PIXEL

    val frameBytes: Int
        get() = rowBytes * height

    private companion object {
        const val BYTES_PER_PIXEL = 4
    }
}
