package org.kvxd.keno.render

import org.kvxd.keno.geometry.Vec2
import org.kvxd.keno.scene.Viewport
import kotlin.test.Test
import kotlin.test.assertEquals

class CanvasProjectionTest {
    @Test
    fun `changing resolution preserves normalized composition`() {
        val point = Vec2(0.75, -0.4)
        val hd = CanvasProjection.fit(Viewport.Widescreen, CanvasSize(1280, 720)).project(point)
        val fullHd = CanvasProjection.fit(Viewport.Widescreen, CanvasSize(1920, 1080)).project(point)

        assertEquals(hd.x / 1280.0, fullHd.x / 1920.0, absoluteTolerance = 0.000_001)
        assertEquals(hd.y / 720.0, fullHd.y / 1080.0, absoluteTolerance = 0.000_001)
    }

    @Test
    fun `non matching output aspect fits without distorting logical units`() {
        val projection = CanvasProjection.fit(Viewport.Widescreen, CanvasSize(1000, 1000))

        assertEquals(projection.scale, projection.project(Vec2(1.0, 0.0)).x - projection.originX)
        assertEquals(projection.scale, projection.originY - projection.project(Vec2(0.0, 1.0)).y)
    }
}
