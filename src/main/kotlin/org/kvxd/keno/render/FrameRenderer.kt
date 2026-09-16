package org.kvxd.keno.render

import org.kvxd.keno.scene.Scene
import org.kvxd.keno.time.FrameContext
import java.nio.ByteBuffer

interface FrameRenderer : AutoCloseable {
    val size: CanvasSize
    val backendName: String

    /** Returns a renderer-owned RGBA buffer valid until the next call to [render]. */
    fun render(scene: Scene, frame: FrameContext): ByteBuffer
}
