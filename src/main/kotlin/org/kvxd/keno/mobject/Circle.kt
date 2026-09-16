package org.kvxd.keno.mobject

import org.jetbrains.skia.Canvas
import org.kvxd.keno.geometry.Vec2

class Circle(
    val radius: Double,
    val style: ShapeStyle = ShapeStyle(),
    position: Vec2 = Vec2.Zero,
    opacity: Double = 1.0,
    zIndex: Int = 0,
) : Mobject(position = position, opacity = opacity, zIndex = zIndex) {
    init {
        require(radius > 0.0) { "Circle radius must be positive" }
    }

    override fun draw(canvas: Canvas, opacity: Double, reveal: Double) {
        val animatedRadius = radius * reveal
        style.fillPaint(opacity * reveal)?.use {
            canvas.drawCircle(0f, 0f, animatedRadius.toFloat(), it)
        }
        style.strokePaint(opacity)?.use {
            canvas.drawArc(
                -animatedRadius.toFloat(),
                -animatedRadius.toFloat(),
                animatedRadius.toFloat(),
                animatedRadius.toFloat(),
                -90f,
                (360.0 * reveal).toFloat(),
                false,
                it,
            )
        }
    }
}
