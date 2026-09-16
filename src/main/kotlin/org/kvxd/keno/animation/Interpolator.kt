package org.kvxd.keno.animation

fun interface Interpolator<T> {
    fun interpolate(start: T, end: T, progress: Double): T
}
