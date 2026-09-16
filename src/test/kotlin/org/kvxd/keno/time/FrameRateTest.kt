package org.kvxd.keno.time

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.nanoseconds

class FrameRateTest {
    @Test
    fun `fifty milliseconds is exactly three frames at sixty fps`() {
        val schedule = FrameSchedule(50.milliseconds, FrameRate(60))

        assertEquals(3, schedule.frameCount)
        assertEquals(50.milliseconds, schedule.outputDuration)
        assertEquals(
            listOf(0.nanoseconds, 16_666_666.nanoseconds, 33_333_333.nanoseconds),
            schedule.map(FrameContext::time),
        )
    }

    @Test
    fun `fractional NTSC rates retain their rational form`() {
        val rate = FrameRate(30_000, 1_001)

        assertEquals("30000/1001", rate.toString())
        assertEquals(30, rate.frameCount(1_001.milliseconds))
        assertEquals(1_001.milliseconds, rate.outputDuration(30))
    }

    @Test
    fun `duration rounds up instead of dropping the final partial frame`() {
        val schedule = FrameSchedule(50.milliseconds, FrameRate(24))

        assertEquals(2, schedule.frameCount)
        assertEquals(83_333_333.nanoseconds, schedule.outputDuration)
    }
}
