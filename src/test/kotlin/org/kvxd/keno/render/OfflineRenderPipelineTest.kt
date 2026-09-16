package org.kvxd.keno.render

import org.jetbrains.skia.Canvas
import org.kvxd.keno.encode.FrameSink
import org.kvxd.keno.scene.Scene
import org.kvxd.keno.time.FrameContext
import org.kvxd.keno.time.FrameRate
import java.nio.ByteBuffer
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds
import kotlin.time.measureTime

class OfflineRenderPipelineTest {
    @Test
    fun `offline rendering samples virtual time without real-time waits`() {
        val sampledTimes = mutableListOf<Duration>()
        var writtenFrames = 0
        val renderer = object : FrameRenderer {
            override val size = CanvasSize(1, 1)
            override val backendName = "test"

            override fun render(scene: Scene, frame: FrameContext): ByteBuffer {
                sampledTimes += frame.time
                return ByteBuffer.allocate(4)
            }

            override fun close() = Unit
        }
        val sink = object : FrameSink {
            override fun write(frame: ByteBuffer) {
                writtenFrames++
            }

            override fun close() = Unit
        }
        val scene = object : Scene {
            override val duration = 5.seconds
            override fun draw(canvas: Canvas, frame: FrameContext) = Unit
        }

        val elapsed = measureTime {
            OfflineRenderPipeline(renderer, sink).render(scene, FrameRate(1))
        }

        assertEquals(5, writtenFrames)
        assertEquals(listOf(0.seconds, 1.seconds, 2.seconds, 3.seconds, 4.seconds), sampledTimes)
        assertTrue(elapsed < 1.seconds, "The pipeline appears to be pacing against the wall clock")
    }
}
