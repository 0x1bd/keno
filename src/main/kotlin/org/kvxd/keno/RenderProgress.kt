package org.kvxd.keno

data class RenderProgress(
    val completedFrames: Long,
    val totalFrames: Long,
) {
    val fraction: Double
        get() = if (totalFrames == 0L) 1.0 else completedFrames.toDouble() / totalFrames
}
