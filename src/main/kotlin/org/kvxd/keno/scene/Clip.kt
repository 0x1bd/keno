package org.kvxd.keno.scene

import kotlin.time.Duration

data class Clip(
    val start: Duration,
    val duration: Duration,
    val zIndex: Int,
    val layer: Layer,
    internal val insertionOrder: Int,
) {
    init {
        require(start.isFinite() && !start.isNegative()) { "Clip start must be finite and non-negative" }
        require(duration.isFinite() && duration.isPositive()) { "Clip duration must be finite and positive" }
    }

    val end: Duration
        get() = start + duration

    fun isActiveAt(time: Duration): Boolean = time >= start && time < end
}
