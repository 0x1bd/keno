package org.kvxd.keno.time

import kotlin.time.Duration

class FrameSchedule(
    val duration: Duration,
    val frameRate: FrameRate,
) : Iterable<FrameContext> {
    val frameCount: Long = frameRate.frameCount(duration)
    val outputDuration: Duration = frameRate.outputDuration(frameCount)

    override fun iterator(): Iterator<FrameContext> =
        (0L until frameCount).asSequence()
            .map { index ->
                FrameContext(
                    index = index,
                    time = frameRate.timestamp(index),
                    sceneDuration = duration,
                    frameRate = frameRate,
                )
            }.iterator()
}
