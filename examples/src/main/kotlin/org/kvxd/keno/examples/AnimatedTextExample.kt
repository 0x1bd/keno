package org.kvxd.keno.examples

import org.jetbrains.skia.Color
import org.jetbrains.skia.FontStyle
import org.kvxd.keno.animation.Easings
import org.kvxd.keno.animation.fadeIn
import org.kvxd.keno.animation.fadeOut
import org.kvxd.keno.animation.moveTo
import org.kvxd.keno.animation.rotateTo
import org.kvxd.keno.animation.scaleTo
import org.kvxd.keno.animation.write
import org.kvxd.keno.geometry.Vec2
import org.kvxd.keno.mobject.Circle
import org.kvxd.keno.mobject.Line
import org.kvxd.keno.mobject.MathText
import org.kvxd.keno.mobject.Rectangle
import org.kvxd.keno.mobject.ShapeStyle
import org.kvxd.keno.mobject.Text
import org.kvxd.keno.mobject.TextStyle
import org.kvxd.keno.scene.Scene
import org.kvxd.keno.scene.motionScene
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

object AnimatedTextExample {
    fun create(): Scene {
        val card = Rectangle(
            width = 2.28,
            height = 0.61,
            cornerRadius = 0.08,
            style = ShapeStyle(
                fillColor = Color.makeARGB(150, 20, 26, 45),
                strokeColor = Color.makeARGB(110, 139, 92, 246),
                strokeWidth = 0.006,
            ),
            position = Vec2(0.0, -0.25),
            opacity = 0.0,
            zIndex = -5,
        )
        val title = Text(
            value = "Title text",
            style = TextStyle(
                fontSize = 0.283,
                color = Color.makeRGB(239, 244, 255),
                fontFamily = "Sans",
                fontStyle = FontStyle.BOLD,
            ),
            position = Vec2.Zero,
            zIndex = 10,
        )
        val underline = Line(
            start = Vec2(-0.78, 0.0),
            end = Vec2(0.78, 0.0),
            color = Color.makeRGB(139, 92, 246),
            strokeWidth = 0.022,
            position = Vec2(0.0, -0.194),
            zIndex = 8,
        )
        val subtitle = Text(
            value = "Subtitle",
            style = TextStyle(fontSize = 0.094, color = Color.makeRGB(165, 178, 205), fontFamily = "Sans"),
            position = Vec2(0.0, 0.417),
            zIndex = 10,
        )
        val formula = MathText(
            expression = "a^2 + b^2 = c^2",
            style = TextStyle(fontSize = 0.217, color = Color.makeRGB(34, 211, 238), fontFamily = "Serif"),
            position = Vec2(0.0, -0.236),
            zIndex = 10,
        )

        return motionScene(backgroundColor = Color.makeRGB(8, 12, 24)) {
            play(title.write(), duration = 1_800.milliseconds, easing = Easings.EaseOut)
            play(underline.write(), duration = 650.milliseconds, easing = Easings.EaseOut)
            wait(450.milliseconds)
            play(
                title.moveTo(Vec2(0.0, 0.667)),
                title.scaleTo(0.55),
                underline.fadeOut(),
                duration = 900.milliseconds,
            )
            play(
                subtitle.fadeIn(from = Vec2(0.0, -0.067)),
                subtitle.write(),
                card.fadeIn(),
                duration = 1_200.milliseconds,
                easing = Easings.EaseOut,
            )
            play(
                formula.fadeIn(from = Vec2(0.0, -0.094)),
                formula.write(),
                duration = 1_500.milliseconds,
                easing = Easings.EaseOut,
            )
            play(formula.scaleTo(1.7), formula.rotateTo(180.0), duration = 500.milliseconds)
            play(formula.scaleTo(1.0), formula.rotateTo(0.0), duration = 500.milliseconds)
            wait(1.seconds)
        }
    }
}
