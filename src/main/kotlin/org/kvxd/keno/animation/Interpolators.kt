package org.kvxd.keno.animation

import org.kvxd.keno.geometry.Vec2

object Interpolators {
    val Double = Interpolator<Double> { start, end, progress -> start + (end - start) * progress }
    val Vec2 = Interpolator<Vec2> { start, end, progress ->
        Vec2(
            x = Double.interpolate(start.x, end.x, progress),
            y = Double.interpolate(start.y, end.y, progress),
        )
    }
}
