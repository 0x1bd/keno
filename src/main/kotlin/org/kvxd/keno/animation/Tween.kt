package org.kvxd.keno.animation

data class Tween<T>(
    val start: T,
    val end: T,
    val interpolator: Interpolator<T>,
    val easing: Easing = Easings.Linear,
) {
    fun valueAt(progress: Double): T {
        val boundedProgress = progress.coerceIn(0.0, 1.0)
        return interpolator.interpolate(start, end, easing.transform(boundedProgress).coerceIn(0.0, 1.0))
    }
}
