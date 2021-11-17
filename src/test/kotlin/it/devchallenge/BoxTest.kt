package it.devchallenge

import org.junit.Test
import kotlin.test.assertEquals

class BoxTest {
    @Test
    fun `should return valid unfolded width & height values`() {
        val boxA = Box(w = 100, d = 100, h = 100)
        val boxB = Box(w = Int.MAX_VALUE, d = 0, h = 0)

        assertEquals(expected = 400, boxA.unfoldedWidth())
        assertEquals(expected = 0, boxB.unfoldedHeight())
    }
}
