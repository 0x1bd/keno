package org.kvxd.keno.scene

import org.jetbrains.skia.Canvas
import org.kvxd.keno.time.FrameContext
import kotlin.time.Duration

class TimelineScene internal constructor(
    override val duration: Duration,
    override val backgroundColor: Int,
    private val clips: List<Clip>,
) : Scene {
    internal fun activeClipsAt(time: Duration): List<Clip> = clips.filter { it.isActiveAt(time) }

    override fun draw(canvas: Canvas, frame: FrameContext) {
        activeClipsAt(frame.time).forEach { clip ->
            canvas.save()
            try {
                clip.layer.draw(
                    canvas,
                    ClipFrame(
                        scene = frame,
                        localTime = frame.time - clip.start,
                        clipDuration = clip.duration,
                    ),
                )
            } finally {
                canvas.restore()
            }
        }
    }
}
