package org.kvxd.keno.render

import org.jetbrains.skia.Canvas
import org.kvxd.keno.scene.Scene
import org.kvxd.keno.time.FrameContext

internal fun Canvas.drawScene(scene: Scene, frame: FrameContext) {
    clear(scene.backgroundColor)
    val restoreCount = save()
    try {
        scene.draw(this, frame)
    } finally {
        restoreToCount(restoreCount)
    }
}
