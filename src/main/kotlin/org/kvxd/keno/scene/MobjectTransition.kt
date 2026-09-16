package org.kvxd.keno.scene

import org.kvxd.keno.animation.Easing
import org.kvxd.keno.animation.MobjectStateInterpolator
import org.kvxd.keno.mobject.MobjectState
import kotlin.time.Duration

internal data class MobjectTransition(
    val start: Duration,
    val duration: Duration,
    val from: MobjectState,
    val to: MobjectState,
    val easing: Easing,
) {
    val end: Duration
        get() = start + duration

    fun stateAt(time: Duration): MobjectState {
        val progress = ((time - start) / duration).coerceIn(0.0, 1.0)
        return MobjectStateInterpolator.interpolate(from, to, easing.transform(progress).coerceIn(0.0, 1.0))
    }
}
