package org.kvxd.keno

import org.kvxd.keno.encode.FfmpegCodec
import java.nio.file.Path
import kotlin.time.Duration

data class RenderReport(
    val output: Path,
    val frameCount: Long,
    val videoDuration: Duration,
    val renderingTime: Duration,
    val renderer: String,
    val codec: FfmpegCodec,
) {
    val realTimeFactor: Double
        get() = if (renderingTime.isPositive()) videoDuration / renderingTime else Double.POSITIVE_INFINITY
}
