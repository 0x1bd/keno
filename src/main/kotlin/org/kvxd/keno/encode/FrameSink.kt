package org.kvxd.keno.encode

import java.nio.ByteBuffer

interface FrameSink : AutoCloseable {
    fun write(frame: ByteBuffer)
}
