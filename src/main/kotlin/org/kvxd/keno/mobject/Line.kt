package org.kvxd.keno.mobject

import org.jetbrains.skia.Canvas
import org.kvxd.keno.geometry.Vec2

class Line(
    val start: Vec2,
    val end: Vec2,
    val color: Int,
    val strokeWidth: Double = 0.01,
    position: Vec2 = Vec2.Zero,
    opacity: Double = 1.0,
    zIndex: Int = 0,
) : Mobject(position = position, opacity = opacity, zIndex = zIndex) {
    init {
        require(strokeWidth > 0.0) { "Line stroke width must be positive" }
    }

    override fun draw(canvas: Canvas, opacity: Double, reveal: Double) {
        val visibleEnd = start + (end - start) * reveal
        ShapeStyle(strokeColor = color, strokeWidth = strokeWidth).strokePaint(opacity)?.use {
            canvas.drawLine(start.x.toFloat(), start.y.toFloat(), visibleEnd.x.toFloat(), visibleEnd.y.toFloat(), it)
        }
    }
}
