package org.kvxd.keno.time

import java.math.BigInteger
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds

data class FrameRate(
    val numerator: Int,
    val denominator: Int = 1,
) {
    init {
        require(numerator > 0) { "Frame-rate numerator must be positive" }
        require(denominator > 0) { "Frame-rate denominator must be positive" }
    }

    val framesPerSecond: Double
        get() = numerator.toDouble() / denominator

    fun frameCount(duration: Duration): Long {
        require(duration.isFinite() && duration.isPositive()) { "Scene duration must be finite and positive" }
        val scaledDuration = duration.inWholeNanoseconds.bi() * numerator.bi()
        val nanosPerRateUnit = NANOS_PER_SECOND * denominator.bi()
        return scaledDuration.ceilDiv(nanosPerRateUnit).longValueExact()
    }

    fun timestamp(frameIndex: Long): Duration {
        require(frameIndex >= 0) { "Frame index cannot be negative" }
        val nanos = frameIndex.bi() * NANOS_PER_SECOND * denominator.bi() / numerator.bi()
        return nanos.longValueExact().nanoseconds
    }

    fun outputDuration(frameCount: Long): Duration {
        require(frameCount >= 0) { "Frame count cannot be negative" }
        val nanos = frameCount.bi() * NANOS_PER_SECOND * denominator.bi() / numerator.bi()
        return nanos.longValueExact().nanoseconds
    }

    override fun toString(): String =
        if (denominator == 1) numerator.toString() else "$numerator/$denominator"

    private fun Long.bi(): BigInteger = BigInteger.valueOf(this)

    private fun Int.bi(): BigInteger = BigInteger.valueOf(toLong())

    private fun BigInteger.ceilDiv(divisor: BigInteger): BigInteger =
        add(divisor).subtract(BigInteger.ONE).divide(divisor)

    private companion object {
        val NANOS_PER_SECOND: BigInteger = BigInteger.valueOf(1_000_000_000L)
    }
}
