package org.kvxd.keno.mobject

import org.jetbrains.skia.Canvas
import org.jetbrains.skia.RRect
import org.kvxd.keno.geometry.Vec2

class Rectangle(
    val width: Double,
    val height: Double,
    val cornerRadius: Double = 0.0,
    val style: ShapeStyle = ShapeStyle(),
    position: Vec2 = Vec2.Zero,
    opacity: Double = 1.0,
    zIndex: Int = 0,
) : Mobject(position = position, opacity = opacity, zIndex = zIndex) {
    init {
        require(width > 0.0 && height > 0.0) { "Rectangle dimensions must be positive" }
        require(cornerRadius >= 0.0) { "Corner radius cannot be negative" }
    }

    override fun draw(canvas: Canvas, opacity: Double, reveal: Double) {
        val animatedWidth = width * reveal
        val animatedHeight = height * reveal
        val rounded = RRect.makeXYWH(
            (-animatedWidth / 2.0).toFloat(),
            (-animatedHeight / 2.0).toFloat(),
            animatedWidth.toFloat(),
            animatedHeight.toFloat(),
            (cornerRadius * reveal).toFloat(),
        )
        style.fillPaint(opacity * reveal)?.use { canvas.drawRRect(rounded, it) }
        style.strokePaint(opacity * reveal)?.use { canvas.drawRRect(rounded, it) }
    }
}
