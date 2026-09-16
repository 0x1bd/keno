package org.kvxd.keno.scene

import org.kvxd.keno.mobject.Mobject
import org.kvxd.keno.mobject.MobjectState
import kotlin.time.Duration

internal class MobjectTrack(
    val mobject: Mobject,
    val insertionOrder: Int,
    val appearanceTime: Duration,
    initialState: MobjectState,
) {
    private val transitions = mutableListOf<MobjectTransition>()
    private var currentState = initialState

    fun append(transition: MobjectTransition) {
        check(transitions.lastOrNull()?.end?.let { it <= transition.start } != false) {
            "Animations on one mobject cannot overlap"
        }
        transitions += transition
        currentState = transition.to
    }

    fun endState(): MobjectState = currentState

    fun stateAt(time: Duration): MobjectState? {
        if (time < appearanceTime) return null
        val active = transitions.firstOrNull { time >= it.start && time < it.end }
        if (active != null) return active.stateAt(time)
        return transitions.lastOrNull { time >= it.end }?.to ?: transitions.firstOrNull()?.from ?: currentState
    }
}
