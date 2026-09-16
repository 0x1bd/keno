package org.kvxd.keno

import org.kvxd.keno.encode.FfmpegCodec
import org.kvxd.keno.render.CanvasSize
import org.kvxd.keno.render.RenderBackend
import org.kvxd.keno.time.FrameRate

data class RenderSettings(
    val size: CanvasSize = CanvasSize(1920, 1080),
    val frameRate: FrameRate = FrameRate(60),
    val backend: RenderBackend = RenderBackend.GPU_OPENGL,
    val codec: FfmpegCodec = FfmpegCodec.AUTO,
    val ffmpegExecutable: String = "ffmpeg",
    val overwrite: Boolean = false,
) {
    init {
        require(size.width % 2 == 0 && size.height % 2 == 0) {
            "H.264 with yuv420p output requires even canvas dimensions"
        }
        require(ffmpegExecutable.isNotBlank()) { "FFmpeg executable cannot be blank" }
    }
}
