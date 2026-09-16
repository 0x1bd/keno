package org.kvxd.keno.animation

import org.kvxd.keno.geometry.Vec2
import org.kvxd.keno.mobject.Mobject
import org.kvxd.keno.mobject.MobjectState

fun Mobject.animate(transform: (MobjectState) -> MobjectState): MobjectAnimation = MobjectAnimation(
    target = this,
    prepareEnd = transform,
)

fun Mobject.write(): MobjectAnimation = MobjectAnimation(
    target = this,
    prepareStart = { it.copy(reveal = 0.0) },
    prepareEnd = { it.copy(reveal = 1.0) },
)

fun Mobject.fadeIn(from: Vec2 = Vec2.Zero): MobjectAnimation = MobjectAnimation(
    target = this,
    prepareStart = { it.copy(position = it.position + from, opacity = 0.0) },
    prepareEnd = { it.copy(opacity = 1.0) },
)

fun Mobject.fadeOut(to: Vec2 = Vec2.Zero): MobjectAnimation = MobjectAnimation(
    target = this,
    prepareEnd = { it.copy(position = it.position + to, opacity = 0.0) },
)

fun Mobject.moveTo(position: Vec2): MobjectAnimation = MobjectAnimation(
    target = this,
    prepareEnd = { it.copy(position = position) },
)

fun Mobject.moveBy(offset: Vec2): MobjectAnimation = MobjectAnimation(
    target = this,
    prepareEnd = { it.copy(position = it.position + offset) },
)

fun Mobject.scaleTo(scale: Double): MobjectAnimation = scaleTo(Vec2(scale, scale))

fun Mobject.scaleTo(scale: Vec2): MobjectAnimation = MobjectAnimation(
    target = this,
    prepareEnd = { it.copy(scale = scale) },
)

fun Mobject.rotateTo(degrees: Double): MobjectAnimation = MobjectAnimation(
    target = this,
    prepareEnd = { it.copy(rotationDegrees = degrees) },
)

fun Mobject.changeOpacityTo(opacity: Double): MobjectAnimation {
    require(opacity in 0.0..1.0) { "Opacity must be between 0 and 1" }
    return MobjectAnimation(
        target = this,
        prepareEnd = { it.copy(opacity = opacity) },
    )
}
