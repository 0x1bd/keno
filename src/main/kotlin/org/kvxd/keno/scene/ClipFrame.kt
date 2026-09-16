package org.kvxd.keno.scene

import org.kvxd.keno.time.FrameContext
import kotlin.time.Duration

data class ClipFrame(
    val scene: FrameContext,
    val localTime: Duration,
    val clipDuration: Duration,
) {
    val progress: Double
        get() = (localTime / clipDuration).coerceIn(0.0, 1.0)
}
