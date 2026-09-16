package org.kvxd.keno.mobject

import org.jetbrains.skia.Color
import org.jetbrains.skia.Paint
import org.jetbrains.skia.PaintMode

internal fun ShapeStyle.fillPaint(opacity: Double): Paint? = fillColor?.let { fill ->
    Paint().apply {
        isAntiAlias = true
        mode = PaintMode.FILL
        color = fill
        alpha = (Color.getA(fill) * opacity.coerceIn(0.0, 1.0)).toInt()
    }
}

internal fun ShapeStyle.strokePaint(opacity: Double): Paint? = if (strokeWidth > 0.0) {
    Paint().apply {
        isAntiAlias = true
        mode = PaintMode.STROKE
        strokeWidth = this@strokePaint.strokeWidth.toFloat()
        color = strokeColor
        alpha = (Color.getA(strokeColor) * opacity.coerceIn(0.0, 1.0)).toInt()
    }
} else {
    null
}
