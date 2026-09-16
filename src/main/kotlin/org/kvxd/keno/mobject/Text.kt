package org.kvxd.keno.mobject

import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.jetbrains.skia.Font
import org.jetbrains.skia.FontMgr
import org.jetbrains.skia.Paint
import org.jetbrains.skia.TextLine
import org.kvxd.keno.animation.Easings
import org.kvxd.keno.geometry.Vec2
import java.text.BreakIterator
import java.util.Locale

open class Text(
    val value: String,
    val style: TextStyle = TextStyle(),
    val alignment: TextAlignment = TextAlignment.CENTER,
    position: Vec2 = Vec2.Zero,
    scale: Vec2 = Vec2(1.0, 1.0),
    rotationDegrees: Double = 0.0,
    opacity: Double = 1.0,
    zIndex: Int = 0,
) : Mobject(position, scale, rotationDegrees, opacity, zIndex) {
    private val font by lazy {
        val typeface = FontMgr.default.matchFamilyStyle(style.fontFamily, style.fontStyle)
        Font(typeface, (style.fontSize * LAYOUT_SCALE).toFloat()).apply {
            isLinearMetrics = true
            isSubpixel = true
        }
    }
    private val glyphs by lazy(::layoutGlyphs)

    val width: Double
        get() = glyphs.maxOfOrNull { it.right }?.minus(glyphs.minOfOrNull { it.left } ?: 0.0) ?: 0.0

    val height: Double
        get() = value.lines().size * style.fontSize * style.lineHeight

    override fun draw(canvas: Canvas, opacity: Double, reveal: Double) {
        if (glyphs.isEmpty()) return
        val paint = Paint().apply {
            isAntiAlias = true
            color = style.color
        }
        canvas.save()
        try {
            canvas.scale(1f, -1f)
            glyphs.forEachIndexed { index, glyph ->
                val glyphProgress = staggeredProgress(reveal, index, glyphs.size)
                if (glyphProgress <= 0.0) return@forEachIndexed
                val eased = Easings.EaseOut.transform(glyphProgress)
                paint.alpha = combinedAlpha(style.color, opacity * eased)
                val scale = 0.82 + 0.18 * eased
                canvas.save()
                try {
                    canvas.translate(
                        glyph.x.toFloat(),
                        (glyph.baseline + (1.0 - eased) * style.fontSize * 0.18).toFloat(),
                    )
                    val glyphScale = scale / LAYOUT_SCALE
                    canvas.scale(glyphScale.toFloat(), glyphScale.toFloat())
                    canvas.drawTextLine(glyph.line, 0f, 0f, paint)
                } finally {
                    canvas.restore()
                }
            }
        } finally {
            canvas.restore()
            paint.close()
        }
    }

    private fun layoutGlyphs(): List<Glyph> {
        val lines = value.lines()
        val lineWidths = lines.map { font.measureTextWidth(it).toDouble() / LAYOUT_SCALE }
        val firstBaseline = -(lines.size - 1) * style.fontSize * style.lineHeight / 2.0 -
            font.metrics.ascent.toDouble() / LAYOUT_SCALE / 2.0
        return buildList {
            lines.forEachIndexed { lineIndex, line ->
                val offset = when (alignment) {
                    TextAlignment.LEFT -> 0.0
                    TextAlignment.CENTER -> -lineWidths[lineIndex] / 2.0
                    TextAlignment.RIGHT -> -lineWidths[lineIndex]
                }
                var x = offset
                graphemes(line).forEach { grapheme ->
                    val textLine = TextLine.make(grapheme, font)
                    add(
                        Glyph(
                            line = textLine,
                            x = x,
                            baseline = firstBaseline + lineIndex * style.fontSize * style.lineHeight,
                            left = x,
                            right = x + textLine.width.toDouble() / LAYOUT_SCALE,
                        ),
                    )
                    x += textLine.width.toDouble() / LAYOUT_SCALE
                }
            }
        }
    }

    private fun graphemes(text: String): List<String> {
        val iterator = BreakIterator.getCharacterInstance(Locale.ROOT)
        iterator.setText(text)
        return buildList {
            var start = iterator.first()
            var end = iterator.next()
            while (end != BreakIterator.DONE) {
                add(text.substring(start, end))
                start = end
                end = iterator.next()
            }
        }
    }

    private fun staggeredProgress(progress: Double, index: Int, count: Int): Double {
        if (progress >= 1.0) return 1.0
        val overlap = 0.35
        val start = if (count <= 1) 0.0 else index.toDouble() / (count - 1) * (1.0 - overlap)
        return ((progress - start) / overlap).coerceIn(0.0, 1.0)
    }

    private fun combinedAlpha(color: Int, opacity: Double): Int =
        (Color.getA(color) * opacity.coerceIn(0.0, 1.0)).toInt()

    private data class Glyph(
        val line: TextLine,
        val x: Double,
        val baseline: Double,
        val left: Double,
        val right: Double,
    )

    private companion object {
        const val LAYOUT_SCALE = 1024.0
    }
}
