package org.kvxd.keno.mobject

import org.kvxd.keno.geometry.Vec2

data class MobjectState(
    val position: Vec2 = Vec2.Zero,
    val scale: Vec2 = Vec2(1.0, 1.0),
    val rotationDegrees: Double = 0.0,
    val opacity: Double = 1.0,
    val reveal: Double = 1.0,
) {
    init {
        require(opacity in 0.0..1.0) { "Opacity must be between 0 and 1" }
        require(reveal in 0.0..1.0) { "Reveal must be between 0 and 1" }
    }
}
