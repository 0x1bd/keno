package org.kvxd.keno.scene

import org.jetbrains.skia.Color
import org.kvxd.keno.animation.Easing
import org.kvxd.keno.animation.Easings
import org.kvxd.keno.animation.MobjectAnimation
import org.kvxd.keno.mobject.Mobject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class MotionSceneBuilder internal constructor(
    private val backgroundColor: Int,
    private val viewport: Viewport,
) {
    private val tracks = linkedMapOf<Mobject, MobjectTrack>()
    private var playhead = Duration.ZERO

    fun add(vararg mobjects: Mobject) {
        mobjects.forEach(::trackFor)
    }

    fun play(
        vararg animations: MobjectAnimation,
        duration: Duration = 1.seconds,
        easing: Easing = Easings.EaseInOut,
    ) {
        require(animations.isNotEmpty()) { "play requires at least one animation" }
        require(duration.isFinite() && duration.isPositive()) { "Animation duration must be finite and positive" }

        animations.groupBy { it.target }.forEach { (mobject, objectAnimations) ->
            val track = trackFor(mobject)
            val base = track.endState()
            val start = objectAnimations.fold(base) { state, animation -> animation.prepareStart(state) }
            val end = objectAnimations.fold(base) { state, animation -> animation.prepareEnd(state) }
            track.append(MobjectTransition(playhead, duration, start, end, easing))
        }
        playhead += duration
    }

    fun wait(duration: Duration = 1.seconds) {
        require(duration.isFinite() && !duration.isNegative()) { "Wait duration must be finite and non-negative" }
        playhead += duration
    }

    internal fun build(): MotionScene {
        require(playhead.isPositive()) { "A motion scene must contain a play or a positive wait" }
        val orderedTracks = tracks.values.sortedWith(compareBy({ it.mobject.zIndex }, MobjectTrack::insertionOrder))
        return MotionScene(playhead, viewport, backgroundColor, orderedTracks)
    }

    private fun trackFor(mobject: Mobject): MobjectTrack = tracks.getOrPut(mobject) {
        MobjectTrack(
            mobject = mobject,
            insertionOrder = tracks.size,
            appearanceTime = playhead,
            initialState = mobject.initialState,
        )
    }
}

fun motionScene(
    backgroundColor: Int = Color.BLACK,
    viewport: Viewport = Viewport.Widescreen,
    configure: MotionSceneBuilder.() -> Unit,
): MotionScene = MotionSceneBuilder(backgroundColor, viewport).apply(configure).build()
