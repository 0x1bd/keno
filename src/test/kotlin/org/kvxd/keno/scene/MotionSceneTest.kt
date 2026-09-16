package org.kvxd.keno.scene

import org.jetbrains.skia.Color
import org.kvxd.keno.animation.Easings
import org.kvxd.keno.animation.fadeIn
import org.kvxd.keno.animation.moveTo
import org.kvxd.keno.animation.scaleTo
import org.kvxd.keno.geometry.Vec2
import org.kvxd.keno.mobject.Circle
import org.kvxd.keno.mobject.ShapeStyle
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class MotionSceneTest {
    @Test
    fun `play advances the scene and keeps the resulting state`() {
        val circle = Circle(0.2, ShapeStyle(strokeColor = Color.WHITE), position = Vec2(0.1, 0.2))
        val scene = motionScene {
            play(circle.moveTo(Vec2(1.1, 0.8)), duration = 2.seconds, easing = Easings.Linear)
            wait(1.seconds)
        }

        assertEquals(3.seconds, scene.duration)
        assertEquals(Vec2(0.6, 0.5), scene.stateOf(circle, 1.seconds)?.position)
        assertEquals(Vec2(1.1, 0.8), scene.stateOf(circle, 2_500.milliseconds)?.position)
    }

    @Test
    fun `simultaneous animations combine on one object`() {
        val circle = Circle(0.2, ShapeStyle(strokeColor = Color.WHITE), position = Vec2(0.3, 0.4))
        val scene = motionScene {
            play(
                circle.fadeIn(from = Vec2(0.0, 0.2)),
                circle.scaleTo(2.0),
                duration = 1.seconds,
                easing = Easings.Linear,
            )
        }

        val start = requireNotNull(scene.stateOf(circle, 0.seconds))
        assertEquals(0.0, start.opacity)
        assertEquals(0.3, start.position.x, absoluteTolerance = 0.000_001)
        assertEquals(0.6, start.position.y, absoluteTolerance = 0.000_001)
        assertEquals(Vec2(1.0, 1.0), start.scale)

        val end = requireNotNull(scene.stateOf(circle, 999.milliseconds))
        assertEquals(2.0, end.scale.x, absoluteTolerance = 0.01)
        assertEquals(1.0, end.opacity, absoluteTolerance = 0.01)
    }

    @Test
    fun `an object does not exist before its first play`() {
        val circle = Circle(0.2, ShapeStyle(strokeColor = Color.WHITE))
        val scene = motionScene {
            wait(1.seconds)
            play(circle.scaleTo(2.0), duration = 1.seconds)
        }

        assertNull(scene.stateOf(circle, 500.milliseconds))
    }
}
