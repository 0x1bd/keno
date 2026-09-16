package org.kvxd.keno.time

import kotlin.time.Duration

data class FrameContext(
    val index: Long,
    val time: Duration,
    val sceneDuration: Duration,
    val frameRate: FrameRate,
) {
    val progress: Double
        get() = (time / sceneDuration).coerceIn(0.0, 1.0)
}
