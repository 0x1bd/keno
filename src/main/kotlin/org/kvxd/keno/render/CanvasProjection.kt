package org.kvxd.keno.render

import org.kvxd.keno.geometry.Vec2
import org.kvxd.keno.scene.Viewport

internal data class CanvasProjection(
    val originX: Double,
    val originY: Double,
    val scale: Double,
) {
    fun project(point: Vec2): Vec2 = Vec2(
        x = originX + point.x * scale,
        y = originY - point.y * scale,
    )

    companion object {
        fun fit(viewport: Viewport, size: CanvasSize): CanvasProjection = CanvasProjection(
            originX = size.width / 2.0,
            originY = size.height / 2.0,
            scale = minOf(size.width / viewport.width, size.height / viewport.height),
        )
    }
}
