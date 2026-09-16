package org.kvxd.keno.scene

import org.jetbrains.skia.Color
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

class TimelineSceneTest {
    @Test
    fun `clips are selected by virtual time and ordered by z index`() {
        val scene = timeline(2.seconds) {
            during(zIndex = 20) { _, _ -> }
            during(start = 500.milliseconds, duration = 1.seconds, zIndex = -1) { _, _ -> }
        }

        assertEquals(listOf(-1, 20), scene.activeClipsAt(750.milliseconds).map(Clip::zIndex))
        assertEquals(listOf(20), scene.activeClipsAt(1_750.milliseconds).map(Clip::zIndex))
        assertEquals(Color.BLACK, scene.backgroundColor)
    }

    @Test
    fun `clip cannot extend past its scene`() {
        assertFailsWith<IllegalArgumentException> {
            timeline(1.seconds) {
                during(start = 750.milliseconds, duration = 500.milliseconds) { _, _ -> }
            }
        }
    }
}
