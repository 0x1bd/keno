package org.kvxd.keno.render

import org.lwjgl.system.APIUtil
import org.lwjgl.system.Callback
import org.lwjgl.system.CallbackI
import org.lwjgl.system.MemoryUtil
import org.lwjgl.system.Pointer
import org.lwjgl.system.libffi.LibFFI
import java.lang.invoke.MethodHandles

@java.lang.FunctionalInterface
internal fun interface GlProcCallbackI : CallbackI {
    fun invoke(context: Long, name: Long): Long

    override fun getDescriptor(): Callback.Descriptor = DESCRIPTOR

    override fun callback(returnValue: Long, arguments: Long) {
        val context = MemoryUtil.memGetAddress(MemoryUtil.memGetAddress(arguments))
        val name = MemoryUtil.memGetAddress(
            MemoryUtil.memGetAddress(arguments + Pointer.POINTER_SIZE),
        )
        APIUtil.apiClosureRetP(returnValue, invoke(context, name))
    }

    companion object {
        @JvmField
        val DESCRIPTOR = Callback.Descriptor(
            GlProcCallbackI::class.java,
            MethodHandles.lookup(),
            APIUtil.apiCreateCIF(
                LibFFI.ffi_type_pointer,
                LibFFI.ffi_type_pointer,
                LibFFI.ffi_type_pointer,
            ),
        )
    }
}
