package org.kvxd.keno.encode

import org.kvxd.keno.render.CanvasSize
import org.kvxd.keno.time.FrameRate
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension

class FfmpegFrameSink(
    executable: String,
    output: Path,
    size: CanvasSize,
    frameRate: FrameRate,
    requestedCodec: FfmpegCodec,
    overwrite: Boolean,
) : FrameSink {
    private val process: Process
    private val input: java.nio.channels.WritableByteChannel
    private val errorOutput = ByteArrayOutputStream()
    private val errorReader: Thread
    private var closed = false

    val codec: FfmpegCodec = FfmpegCodecSelector(executable).resolve(requestedCodec)

    init {
        val normalizedOutput = output.toAbsolutePath().normalize()
        Files.createDirectories(normalizedOutput.parent)
        if (!overwrite && Files.exists(normalizedOutput)) {
            throw FfmpegException("Output already exists: $normalizedOutput")
        }
        val command = buildList {
            add(executable)
            addAll(listOf("-hide_banner", "-loglevel", "error"))
            add(if (overwrite) "-y" else "-n")
            addAll(listOf("-f", "rawvideo", "-pixel_format", "rgba"))
            addAll(listOf("-video_size", "${size.width}x${size.height}"))
            addAll(listOf("-framerate", frameRate.toString(), "-i", "pipe:0", "-an"))
            addAll(listOf("-c:v", codec.encoderName!!))
            addAll(codec.arguments)
            addAll(listOf("-pix_fmt", "yuv420p"))
            if (normalizedOutput.extension.lowercase() in setOf("mp4", "mov", "m4v")) {
                addAll(listOf("-movflags", "+faststart", "-video_track_timescale", frameRate.numerator.toString()))
            }
            add(normalizedOutput.toString())
        }

        try {
            process = ProcessBuilder(command).start()
        } catch (failure: Exception) {
            throw FfmpegException("Could not start FFmpeg executable '$executable'", failure)
        }
        input = Channels.newChannel(process.outputStream)
        errorReader = Thread({ process.errorStream.use { it.copyTo(errorOutput) } }, "keno-ffmpeg-errors").apply {
            isDaemon = true
            start()
        }
    }

    override fun write(frame: ByteBuffer) {
        check(!closed) { "FFmpeg sink is closed" }
        val readableFrame = frame.duplicate()
        try {
            while (readableFrame.hasRemaining()) input.write(readableFrame)
        } catch (failure: Exception) {
            throw FfmpegException("FFmpeg stopped while receiving a video frame", failure)
        }
    }

    override fun close() {
        if (closed) return
        closed = true
        val pipeFailure = runCatching(input::close).exceptionOrNull()
        val exitCode = process.waitFor()
        errorReader.join()
        if (exitCode != 0) {
            val details = errorOutput.toString(Charsets.UTF_8).trim()
            throw FfmpegException(
                "FFmpeg exited with code $exitCode${if (details.isEmpty()) "" else ": $details"}",
                pipeFailure,
            )
        }
        if (pipeFailure != null) throw FfmpegException("Could not close the FFmpeg frame pipe", pipeFailure)
    }
}
