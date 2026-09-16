package org.kvxd.keno.render

import org.kvxd.keno.scene.Scene
import org.kvxd.keno.time.FrameContext
import org.lwjgl.glfw.GLFW.glfwMakeContextCurrent
import org.lwjgl.opengl.GL11.GL_PACK_ALIGNMENT
import org.lwjgl.opengl.GL11.GL_RGBA
import org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE
import org.lwjgl.opengl.GL11.glGetString
import org.lwjgl.opengl.GL11.glPixelStorei
import org.lwjgl.opengl.GL11.glReadPixels
import org.lwjgl.opengl.GL20.GL_SHADING_LANGUAGE_VERSION
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER
import org.lwjgl.opengl.GL30.glBindFramebuffer
import org.lwjgl.system.MemoryUtil
import java.nio.ByteBuffer

class OpenGlSkiaRenderer(
    override val size: CanvasSize,
) : FrameRenderer {
    private val ownerThread = Thread.currentThread()
    private val resources = OpenGlResources.create(size)
    private val glPixels = MemoryUtil.memAlloc(size.frameBytes)
    private val outputPixels = MemoryUtil.memAlloc(size.frameBytes)
    private var closed = false

    override val backendName: String =
        "Skia/OpenGL ${glGetString(GL_SHADING_LANGUAGE_VERSION) ?: "GPU"}"

    init {
        glPixelStorei(GL_PACK_ALIGNMENT, 1)
    }

    override fun render(scene: Scene, frame: FrameContext): ByteBuffer {
        ensureUsable()
        glfwMakeContextCurrent(resources.window)
        glBindFramebuffer(GL_FRAMEBUFFER, resources.framebuffer)
        resources.surface.canvas.drawScene(scene, frame, size)
        resources.surface.flushAndSubmit()
        glPixels.clear()
        glReadPixels(0, 0, size.width, size.height, GL_RGBA, GL_UNSIGNED_BYTE, glPixels)
        flipRows()
        return outputPixels.clear().limit(size.frameBytes)
    }

    private fun flipRows() {
        val sourceAddress = MemoryUtil.memAddress(glPixels)
        val targetAddress = MemoryUtil.memAddress(outputPixels)
        for (sourceRow in 0 until size.height) {
            val targetRow = size.height - sourceRow - 1
            MemoryUtil.memCopy(
                sourceAddress + sourceRow.toLong() * size.rowBytes,
                targetAddress + targetRow.toLong() * size.rowBytes,
                size.rowBytes.toLong(),
            )
        }
    }

    private fun ensureUsable() {
        check(!closed) { "Renderer is closed" }
        check(Thread.currentThread() === ownerThread) {
            "OpenGL renderer must be used and closed on the thread that created it"
        }
    }

    override fun close() {
        if (closed) return
        ensureUsable()
        closed = true
        MemoryUtil.memFree(glPixels)
        MemoryUtil.memFree(outputPixels)
        resources.close()
    }
}
