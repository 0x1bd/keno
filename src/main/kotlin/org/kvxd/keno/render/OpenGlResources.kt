package org.kvxd.keno.render

import org.jetbrains.skia.BackendRenderTarget
import org.jetbrains.skia.ColorSpace
import org.jetbrains.skia.DirectContext
import org.jetbrains.skia.FramebufferFormat
import org.jetbrains.skia.GLAssembledInterface
import org.jetbrains.skia.Surface
import org.jetbrains.skia.SurfaceColorFormat
import org.jetbrains.skia.SurfaceOrigin
import org.jetbrains.skia.makeGLWithInterface
import org.lwjgl.glfw.GLFW.GLFW_FALSE
import org.lwjgl.glfw.GLFW.GLFW_VISIBLE
import org.lwjgl.glfw.GLFW.glfwCreateWindow
import org.lwjgl.glfw.GLFW.glfwDefaultWindowHints
import org.lwjgl.glfw.GLFW.glfwDestroyWindow
import org.lwjgl.glfw.GLFW.glfwMakeContextCurrent
import org.lwjgl.glfw.GLFW.glfwSwapInterval
import org.lwjgl.glfw.GLFW.glfwWindowHint
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.GL_LINEAR
import org.lwjgl.opengl.GL11.GL_RGBA
import org.lwjgl.opengl.GL11.GL_RGBA8
import org.lwjgl.opengl.GL11.GL_TEXTURE_2D
import org.lwjgl.opengl.GL11.GL_TEXTURE_MAG_FILTER
import org.lwjgl.opengl.GL11.GL_TEXTURE_MIN_FILTER
import org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE
import org.lwjgl.opengl.GL11.glBindTexture
import org.lwjgl.opengl.GL11.glDeleteTextures
import org.lwjgl.opengl.GL11.glGenTextures
import org.lwjgl.opengl.GL11.glTexImage2D
import org.lwjgl.opengl.GL11.glTexParameteri
import org.lwjgl.opengl.GL30.GL_COLOR_ATTACHMENT0
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER
import org.lwjgl.opengl.GL30.GL_FRAMEBUFFER_COMPLETE
import org.lwjgl.opengl.GL30.glBindFramebuffer
import org.lwjgl.opengl.GL30.glCheckFramebufferStatus
import org.lwjgl.opengl.GL30.glDeleteFramebuffers
import org.lwjgl.opengl.GL30.glFramebufferTexture2D
import org.lwjgl.opengl.GL30.glGenFramebuffers
import org.lwjgl.system.MemoryUtil

internal class OpenGlResources private constructor(
    val window: Long,
    val framebuffer: Int,
    val texture: Int,
    val context: DirectContext,
    val renderTarget: BackendRenderTarget,
    val surface: Surface,
) : AutoCloseable {
    override fun close() {
        glfwMakeContextCurrent(window)
        surface.close()
        renderTarget.close()
        context.close()
        glDeleteFramebuffers(framebuffer)
        glDeleteTextures(texture)
        GL.setCapabilities(null)
        glfwDestroyWindow(window)
        GlfwRuntime.release()
    }

    companion object {
        fun create(size: CanvasSize): OpenGlResources {
            var window = 0L
            var framebuffer = 0
            var texture = 0
            var context: DirectContext? = null
            var renderTarget: BackendRenderTarget? = null
            var surface: Surface? = null
            var procCallback: GlProcCallback? = null
            var capabilitiesCreated = false

            GlfwRuntime.acquire()
            try {
                glfwDefaultWindowHints()
                glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
                window = glfwCreateWindow(size.width, size.height, "Keno offscreen renderer", 0L, 0L)
                check(window != 0L) { "Could not create an offscreen OpenGL context" }
                glfwMakeContextCurrent(window)
                glfwSwapInterval(0)
                GL.createCapabilities()
                capabilitiesCreated = true

                texture = glGenTextures()
                glBindTexture(GL_TEXTURE_2D, texture)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
                glTexImage2D(
                    GL_TEXTURE_2D,
                    0,
                    GL_RGBA8,
                    size.width,
                    size.height,
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    0L,
                )

                framebuffer = glGenFramebuffers()
                glBindFramebuffer(GL_FRAMEBUFFER, framebuffer)
                glFramebufferTexture2D(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0, GL_TEXTURE_2D, texture, 0)
                check(glCheckFramebufferStatus(GL_FRAMEBUFFER) == GL_FRAMEBUFFER_COMPLETE) {
                    "OpenGL could not create a complete offscreen framebuffer"
                }

                val functionProvider = checkNotNull(GL.getFunctionProvider()) {
                    "LWJGL did not expose an OpenGL function provider"
                }
                procCallback = object : GlProcCallback() {
                    override fun invoke(context: Long, name: Long): Long =
                        functionProvider.getFunctionAddress(MemoryUtil.memUTF8(name))
                }
                lateinit var directContext: DirectContext
                GLAssembledInterface.createFromNativePointers(0L, procCallback.address()).use { glInterface ->
                    directContext = DirectContext.makeGLWithInterface(glInterface)
                }
                context = directContext
                procCallback.free()
                procCallback = null
                renderTarget = BackendRenderTarget.makeGL(
                    size.width,
                    size.height,
                    0,
                    0,
                    framebuffer,
                    FramebufferFormat.GR_GL_RGBA8,
                )
                surface = Surface.makeFromBackendRenderTarget(
                    directContext,
                    renderTarget,
                    SurfaceOrigin.BOTTOM_LEFT,
                    SurfaceColorFormat.RGBA_8888,
                    ColorSpace.sRGB,
                ) ?: error("Skia could not wrap the OpenGL framebuffer")

                return OpenGlResources(window, framebuffer, texture, directContext, renderTarget, surface)
            } catch (failure: Throwable) {
                procCallback?.free()
                surface?.close()
                renderTarget?.close()
                context?.close()
                if (capabilitiesCreated) {
                    if (framebuffer != 0) glDeleteFramebuffers(framebuffer)
                    if (texture != 0) glDeleteTextures(texture)
                    GL.setCapabilities(null)
                }
                if (window != 0L) glfwDestroyWindow(window)
                GlfwRuntime.release()
                throw failure
            }
        }
    }
}
