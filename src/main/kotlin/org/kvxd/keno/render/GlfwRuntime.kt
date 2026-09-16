package org.kvxd.keno.render

import org.lwjgl.glfw.GLFW.glfwInit
import org.lwjgl.glfw.GLFW.glfwTerminate

internal object GlfwRuntime {
    private var references = 0

    @Synchronized
    fun acquire() {
        if (references == 0 && !glfwInit()) {
            error("GLFW could not initialize. A display server and OpenGL driver are required for GPU rendering.")
        }
        references++
    }

    @Synchronized
    fun release() {
        check(references > 0) { "GLFW runtime released without a matching acquire" }
        references--
        if (references == 0) glfwTerminate()
    }
}
