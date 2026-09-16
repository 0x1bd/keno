package org.kvxd.keno.animation

fun interface Easing {
    fun transform(progress: Double): Double
}
