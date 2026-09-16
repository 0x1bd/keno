package org.kvxd.keno.examples

import org.kvxd.keno.scene.Scene

data class Example(
    val name: String,
    val description: String,
    val scene: () -> Scene,
)
