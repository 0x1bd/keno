package org.kvxd.keno.animation

import kotlin.math.pow

object Easings {
    val Linear = Easing { it }
    val EaseIn = Easing { it * it * it }
    val EaseOut = Easing { 1.0 - (1.0 - it).pow(3) }
    val EaseInOut = Easing { progress ->
        if (progress < 0.5) {
            4.0 * progress * progress * progress
        } else {
            1.0 - (-2.0 * progress + 2.0).pow(3) / 2.0
        }
    }
}
