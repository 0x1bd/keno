package org.kvxd.keno.mobject

import org.jetbrains.skia.Canvas
import org.kvxd.keno.geometry.Vec2

abstract class Mobject(
    position: Vec2 = Vec2.Zero,
    scale: Vec2 = Vec2(1.0, 1.0),
    rotationDegrees: Double = 0.0,
    opacity: Double = 1.0,
    val zIndex: Int = 0,
) {
    val initialState = MobjectState(
        position = position,
        scale = scale,
        rotationDegrees = rotationDegrees,
        opacity = opacity,
    )

    internal fun render(canvas: Canvas, state: MobjectState) {
        if (state.opacity <= 0.0 || state.reveal <= 0.0) return
        canvas.save()
        try {
            canvas.translate(state.position.x.toFloat(), state.position.y.toFloat())
            canvas.rotate(state.rotationDegrees.toFloat())
            canvas.scale(state.scale.x.toFloat(), state.scale.y.toFloat())
            draw(canvas, state.opacity, state.reveal)
        } finally {
            canvas.restore()
        }
    }

    protected abstract fun draw(canvas: Canvas, opacity: Double, reveal: Double)
}
