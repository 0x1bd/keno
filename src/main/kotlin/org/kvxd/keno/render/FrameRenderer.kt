package org.kvxd.keno.render

import org.kvxd.keno.scene.Scene
import org.kvxd.keno.time.FrameContext
import java.nio.ByteBuffer

interface FrameRenderer : AutoCloseable {
    val size: CanvasSize
    val backendName: String

    fun render(scene: Scene, frame: FrameContext): ByteBuffer
}
