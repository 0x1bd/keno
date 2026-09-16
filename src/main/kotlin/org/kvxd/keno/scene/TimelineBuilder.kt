package org.kvxd.keno.scene

import org.jetbrains.skia.Color
import kotlin.time.Duration

class TimelineBuilder internal constructor(
    private val duration: Duration,
    private val backgroundColor: Int,
    private val viewport: Viewport,
) {
    private val clips = mutableListOf<Clip>()

    fun during(
        start: Duration = Duration.ZERO,
        duration: Duration = this.duration - start,
        zIndex: Int = 0,
        draw: Layer,
    ) {
        val clip = Clip(start, duration, zIndex, draw, clips.size)
        require(clip.end <= this.duration) {
            "Clip ending at ${clip.end} exceeds scene duration ${this.duration}"
        }
        clips += clip
    }

    internal fun build(): TimelineScene =
        TimelineScene(
            duration = duration,
            viewport = viewport,
            backgroundColor = backgroundColor,
            clips = clips.sortedWith(compareBy(Clip::zIndex, Clip::insertionOrder)),
        )
}

fun timeline(
    duration: Duration,
    backgroundColor: Int = Color.BLACK,
    viewport: Viewport = Viewport.Widescreen,
    configure: TimelineBuilder.() -> Unit,
): TimelineScene {
    require(duration.isFinite() && duration.isPositive()) { "Scene duration must be finite and positive" }
    return TimelineBuilder(duration, backgroundColor, viewport).apply(configure).build()
}
