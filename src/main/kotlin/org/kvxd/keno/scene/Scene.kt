package org.kvxd.keno.scene

import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.kvxd.keno.time.FrameContext
import kotlin.time.Duration

interface Scene {
    val duration: Duration

    val viewport: Viewport
        get() = Viewport.Widescreen

    val backgroundColor: Int
        get() = Color.BLACK

    fun draw(canvas: Canvas, frame: FrameContext)
}
