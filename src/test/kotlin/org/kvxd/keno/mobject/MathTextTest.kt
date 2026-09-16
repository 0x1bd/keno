package org.kvxd.keno.mobject

import kotlin.test.Test
import kotlin.test.assertEquals

class MathTextTest {
    @Test
    fun `formats common mathematical notation without external tools`() {
        assertEquals("α² + β₂ ≤ ∞", MathText("\\alpha^2 + \\beta_2 \\le \\infty").value)
    }
}
