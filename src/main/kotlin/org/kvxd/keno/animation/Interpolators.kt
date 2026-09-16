package org.kvxd.keno.animation

import org.jetbrains.skia.Point

object Interpolators {
    val Double = Interpolator<Double> { start, end, progress -> start + (end - start) * progress }
    val Float = Interpolator<Float> { start, end, progress -> start + (end - start) * progress.toFloat() }
    val Point = Interpolator<Point> { start, end, progress ->
        org.jetbrains.skia.Point(
            x = Float.interpolate(start.x, end.x, progress),
            y = Float.interpolate(start.y, end.y, progress),
        )
    }
}
