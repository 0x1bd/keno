package org.kvxd.keno.animation

import org.kvxd.keno.mobject.Mobject
import org.kvxd.keno.mobject.MobjectState

class MobjectAnimation internal constructor(
    internal val target: Mobject,
    internal val prepareStart: (MobjectState) -> MobjectState = { it },
    internal val prepareEnd: (MobjectState) -> MobjectState,
)
