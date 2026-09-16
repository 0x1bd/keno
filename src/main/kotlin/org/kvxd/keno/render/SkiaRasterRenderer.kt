package org.kvxd.keno.render

import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorSpace
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.ImageInfo
import org.jetbrains.skia.Surface
import org.kvxd.keno.scene.Scene
import org.kvxd.keno.time.FrameContext
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

class SkiaRasterRenderer(
    override val size: CanvasSize,
) : FrameRenderer {
    private val pixels: ByteBuffer = MemoryUtil.memAlloc(size.frameBytes)
    private val imageInfo = ImageInfo(
        size.width,
        size.height,
        ColorType.RGBA_8888,
        ColorAlphaType.PREMUL,
        ColorSpace.sRGB,
    )
    private val surface = Surface.makeRasterDirect(imageInfo, MemoryUtil.memAddress(pixels), size.rowBytes)
    private var closed = false

    override val backendName: String = "Skia raster"

    override fun render(scene: Scene, frame: FrameContext): ByteBuffer {
        check(!closed) { "Renderer is closed" }
        surface.canvas.drawScene(scene, frame)
        surface.flushAndSubmit()
        return pixels.clear().limit(size.frameBytes)
    }

    override fun close() {
        if (closed) return
        closed = true
        surface.close()
        MemoryUtil.memFree(pixels)
    }
}
