package org.kvxd.keno.geometry

data class Vec2(
    val x: Double,
    val y: Double,
) {
    operator fun plus(other: Vec2): Vec2 = Vec2(x + other.x, y + other.y)

    operator fun minus(other: Vec2): Vec2 = Vec2(x - other.x, y - other.y)

    operator fun times(factor: Double): Vec2 = Vec2(x * factor, y * factor)

    companion object {
        val Zero = Vec2(0.0, 0.0)
    }
}
