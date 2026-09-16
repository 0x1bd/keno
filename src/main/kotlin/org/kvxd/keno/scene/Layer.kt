package org.kvxd.keno.scene

import org.jetbrains.skia.Canvas

fun interface Layer {
    fun draw(canvas: Canvas, frame: ClipFrame)
}
