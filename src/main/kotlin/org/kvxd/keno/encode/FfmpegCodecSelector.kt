package org.kvxd.keno.encode

import java.util.Locale
import java.util.concurrent.TimeUnit

internal class FfmpegCodecSelector(
    private val executable: String,
) {
    fun resolve(requested: FfmpegCodec): FfmpegCodec {
        if (requested != FfmpegCodec.AUTO) {
            require(probe(requested)) {
                "FFmpeg encoder ${requested.encoderName} is unavailable or cannot access its hardware"
            }
            return requested
        }

        return candidates().firstOrNull(::probe)
            ?: throw FfmpegException("FFmpeg is installed, but no supported H.264 encoder could encode a test frame")
    }

    private fun candidates(): List<FfmpegCodec> {
        val os = System.getProperty("os.name").lowercase(Locale.ROOT)
        return buildList {
            add(FfmpegCodec.H264_NVIDIA)
            add(FfmpegCodec.H264_INTEL)
            if ("windows" in os) add(FfmpegCodec.H264_AMD)
            if ("mac" in os) add(FfmpegCodec.H264_APPLE)
            add(FfmpegCodec.H264)
        }
    }

    private fun probe(codec: FfmpegCodec): Boolean = try {
        val command = buildList {
            add(executable)
            addAll(listOf("-hide_banner", "-loglevel", "error"))
            addAll(listOf("-f", "lavfi", "-i", "color=size=16x16:rate=1"))
            addAll(listOf("-frames:v", "1", "-an", "-c:v", codec.encoderName!!))
            addAll(codec.arguments)
            addAll(listOf("-pix_fmt", "yuv420p", "-f", "null", "-"))
        }
        val process = ProcessBuilder(command).redirectErrorStream(true).start()
        process.inputStream.use { it.readAllBytes() }
        process.waitFor(10, TimeUnit.SECONDS) && process.exitValue() == 0
    } catch (_: Exception) {
        false
    }
}
