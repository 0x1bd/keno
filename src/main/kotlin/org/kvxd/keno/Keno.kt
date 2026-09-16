package org.kvxd.keno

import org.kvxd.keno.encode.FfmpegFrameSink
import org.kvxd.keno.render.FrameRenderer
import org.kvxd.keno.render.OpenGlSkiaRenderer
import org.kvxd.keno.render.OfflineRenderPipeline
import org.kvxd.keno.render.RenderBackend
import org.kvxd.keno.render.SkiaRasterRenderer
import org.kvxd.keno.scene.Scene
import org.kvxd.keno.time.FrameSchedule
import java.nio.file.Path
import kotlin.time.measureTime

object Keno {
    fun render(
        scene: Scene,
        output: Path,
        settings: RenderSettings = RenderSettings(),
        onProgress: (RenderProgress) -> Unit = {},
    ): RenderReport {
        val schedule = FrameSchedule(scene.duration, settings.frameRate)
        val renderer = try {
            createRenderer(settings)
        } catch (failure: Throwable) {
            val hint = if (settings.backend == RenderBackend.GPU_OPENGL) {
                " Use RenderBackend.RASTER explicitly if this system has no accessible GPU/display."
            } else {
                ""
            }
            throw IllegalStateException("Keno could not initialize ${settings.backend}.$hint", failure)
        }
        var selectedCodec = settings.codec
        val elapsed = measureTime {
            renderer.use { activeRenderer ->
                FfmpegFrameSink(
                    executable = settings.ffmpegExecutable,
                    output = output,
                    size = settings.size,
                    frameRate = settings.frameRate,
                    requestedCodec = settings.codec,
                    overwrite = settings.overwrite,
                ).use { encoder ->
                    selectedCodec = encoder.codec
                    OfflineRenderPipeline(activeRenderer, encoder).render(scene, schedule, onProgress)
                }
            }
        }

        return RenderReport(
            output = output.toAbsolutePath().normalize(),
            frameCount = schedule.frameCount,
            videoDuration = schedule.outputDuration,
            renderingTime = elapsed,
            renderer = renderer.backendName,
            codec = selectedCodec,
        )
    }

    private fun createRenderer(settings: RenderSettings): FrameRenderer = when (settings.backend) {
        RenderBackend.GPU_OPENGL -> OpenGlSkiaRenderer(settings.size)
        RenderBackend.RASTER -> SkiaRasterRenderer(settings.size)
    }
}
