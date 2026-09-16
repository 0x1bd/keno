package org.kvxd.keno.scene

import org.jetbrains.skia.Canvas
import org.kvxd.keno.mobject.Mobject
import org.kvxd.keno.mobject.MobjectState
import org.kvxd.keno.time.FrameContext
import kotlin.time.Duration

class MotionScene internal constructor(
    override val duration: Duration,
    override val viewport: Viewport,
    override val backgroundColor: Int,
    private val tracks: List<MobjectTrack>,
) : Scene {
    override fun draw(canvas: Canvas, frame: FrameContext) {
        tracks.forEach { track ->
            track.stateAt(frame.time)?.let { state -> track.mobject.render(canvas, state) }
        }
    }

    internal fun stateOf(mobject: Mobject, time: Duration): MobjectState? =
        tracks.firstOrNull { it.mobject === mobject }?.stateAt(time)
}
