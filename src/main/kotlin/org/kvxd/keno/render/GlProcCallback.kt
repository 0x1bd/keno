package org.kvxd.keno.render

import org.lwjgl.system.Callback

internal abstract class GlProcCallback : Callback(GlProcCallbackI.DESCRIPTOR), GlProcCallbackI {
    override fun address(): Long = super<Callback>.address()
}
