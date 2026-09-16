package org.kvxd.keno.animation

import kotlin.test.Test
import kotlin.test.assertEquals

class TweenTest {
    private val tween = Tween(10.0, 20.0, Interpolators.Double)

    @Test
    fun `interpolates values`() {
        assertEquals(12.5, tween.valueAt(0.25))
    }

    @Test
    fun `clamps values outside the animation`() {
        assertEquals(10.0, tween.valueAt(-1.0))
        assertEquals(20.0, tween.valueAt(2.0))
    }
}
