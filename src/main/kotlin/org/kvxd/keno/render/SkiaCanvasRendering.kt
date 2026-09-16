package org.kvxd.keno.render

import org.jetbrains.skia.Canvas
import org.jetbrains.skia.Rect
import org.kvxd.keno.scene.Scene
import org.kvxd.keno.time.FrameContext

internal fun Canvas.drawScene(scene: Scene, frame: FrameContext, size: CanvasSize) {
    clear(scene.backgroundColor)
    val restoreCount = save()
    try {
        val viewport = scene.viewport
        val projection = CanvasProjection.fit(viewport, size)
        translate(projection.originX.toFloat(), projection.originY.toFloat())
        scale(projection.scale.toFloat(), -projection.scale.toFloat())
        clipRect(
            Rect.makeLTRB(
                viewport.left.toFloat(),
                viewport.bottom.toFloat(),
                viewport.right.toFloat(),
                viewport.top.toFloat(),
            ),
        )
        scene.draw(this, frame)
    } finally {
        restoreToCount(restoreCount)
    }
}
