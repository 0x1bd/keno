package org.kvxd.keno.examples

import org.kvxd.keno.Keno
import org.kvxd.keno.RenderSettings
import org.kvxd.keno.render.CanvasSize
import org.kvxd.keno.render.RenderBackend
import org.kvxd.keno.time.FrameRate
import java.nio.file.Path
import kotlin.io.path.absolute

private val examples = listOf(
    Example(
        name = "animated-text",
        description = "Manim-inspired animated typography, shapes, transforms, and math",
        scene = AnimatedTextExample::create,
    ),
)

fun main(args: Array<String>) {
    val requestedName = args.firstOrNull()
    if (requestedName == null || requestedName == "--list") {
        println("Available Keno examples:")
        examples.forEach { println("  ${it.name.padEnd(18)} ${it.description}") }
        println("\nRun one with: ./gradlew examples:run --args=\"animated-text [output] [--size=1920x1080]\"")
        return
    }

    val example = examples.firstOrNull { it.name == requestedName }
        ?: error("Unknown example '$requestedName'. Available: ${examples.joinToString { it.name }}")
    val options = args.drop(1)
    val output = options.firstOrNull { !it.startsWith("--") }?.let(Path::of)
        ?: Path.of("build", "videos", "${example.name}.mp4")
    val size = options.firstOrNull { it.startsWith("--size=") }
        ?.substringAfter('=')
        ?.let(::parseSize)
        ?: CanvasSize(1920, 1080)
    var lastPercent = -1
    println("Rendering ${example.name} at ${size.width}x${size.height} to ${output.absolute()} …")
    val report = Keno.render(
        scene = example.scene(),
        output = output,
        settings = RenderSettings(
            size = size,
            frameRate = FrameRate(30),
            backend = RenderBackend.RASTER,
            overwrite = true,
        ),
    ) { progress ->
        val percent = (progress.fraction * 100).toInt()
        if (percent >= lastPercent + 10 || percent == 100) {
            print("${percent.coerceAtMost(100)}% ")
            lastPercent = percent
        }
    }
    println("\nWrote ${report.frameCount} frames to ${report.output} in ${report.renderingTime}")
}

private fun parseSize(value: String): CanvasSize {
    val dimensions = value.lowercase().split('x', limit = 2)
    require(dimensions.size == 2) { "Size must use WIDTHxHEIGHT, for example 1920x1080" }
    return CanvasSize(dimensions[0].toInt(), dimensions[1].toInt())
}
