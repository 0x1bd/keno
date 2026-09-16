package org.kvxd.keno.render

import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Color
import org.kvxd.keno.scene.Scene
import org.kvxd.keno.time.FrameContext
import org.kvxd.keno.time.FrameRate
import org.kvxd.keno.time.FrameSchedule
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.time.Duration.Companion.milliseconds

class SkiaRasterRendererTest {
    @Test
    fun `Skia renders RGBA frames into the transport buffer`() {
        val scene = object : Scene {
            override val duration = 50.milliseconds
            override val backgroundColor = Color.RED
            override fun draw(canvas: Canvas, frame: FrameContext) = Unit
        }
        val frame = FrameSchedule(scene.duration, FrameRate(60)).first()

        val pixel = SkiaRasterRenderer(CanvasSize(1, 1)).use { renderer ->
            ByteArray(4).also(renderer.render(scene, frame)::get)
        }

        assertContentEquals(byteArrayOf(0xff.toByte(), 0, 0, 0xff.toByte()), pixel)
    }
}
