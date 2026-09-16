package org.kvxd.keno.render

import org.kvxd.keno.RenderProgress
import org.kvxd.keno.encode.FrameSink
import org.kvxd.keno.scene.Scene
import org.kvxd.keno.time.FrameRate
import org.kvxd.keno.time.FrameSchedule

class OfflineRenderPipeline(
    private val renderer: FrameRenderer,
    private val sink: FrameSink,
) {
    fun render(
        scene: Scene,
        frameRate: FrameRate,
        onProgress: (RenderProgress) -> Unit = {},
    ): FrameSchedule {
        val schedule = FrameSchedule(scene.duration, frameRate)
        render(scene, schedule, onProgress)
        return schedule
    }

    fun render(
        scene: Scene,
        schedule: FrameSchedule,
        onProgress: (RenderProgress) -> Unit = {},
    ) {
        require(schedule.duration == scene.duration) {
            "Frame schedule duration ${schedule.duration} does not match scene duration ${scene.duration}"
        }
        schedule.forEach { frame ->
            sink.write(renderer.render(scene, frame))
            onProgress(RenderProgress(frame.index + 1, schedule.frameCount))
        }
    }
}
