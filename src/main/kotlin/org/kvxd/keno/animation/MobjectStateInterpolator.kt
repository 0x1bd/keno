package org.kvxd.keno.animation

import org.kvxd.keno.geometry.Vec2
import org.kvxd.keno.mobject.MobjectState

internal object MobjectStateInterpolator {
    fun interpolate(start: MobjectState, end: MobjectState, progress: Double): MobjectState =
        MobjectState(
            position = interpolate(start.position, end.position, progress),
            scale = interpolate(start.scale, end.scale, progress),
            rotationDegrees = Interpolators.Double.interpolate(start.rotationDegrees, end.rotationDegrees, progress),
            opacity = Interpolators.Double.interpolate(start.opacity, end.opacity, progress),
            reveal = Interpolators.Double.interpolate(start.reveal, end.reveal, progress),
        )

    private fun interpolate(start: Vec2, end: Vec2, progress: Double): Vec2 =
        Vec2(
            x = Interpolators.Double.interpolate(start.x, end.x, progress),
            y = Interpolators.Double.interpolate(start.y, end.y, progress),
        )
}
