package it.devchallenge.cnc

import it.devchallenge.Box
import java.awt.Point

data class BoxPattern(private val box: Box) {
    private val path = with(box) {
        listOf(
            Point(box.h, 0),
            Point(box.h, box.h),
            Point(0, h),
            Point(0, h + d),
            Point(h, h + d),
            Point(h, 2 * h + d),
            Point(h + w, 2 * box.h + box.d),
            Point(h + w, h + d),
            Point(2 * (h + w), h + d),
            Point(2 * (h + w), h),
            Point(h + w, h),
            Point(h + w, 0),
            Point(h, 0)
        )
    }

    fun contourPath() = path
    fun width() = box.unfoldedWidth()
    fun height() = box.unfoldedHeight()
    fun shift(dx: Int, dy: Int) = path.onEach { it.translate(dx, dy) }
}

data class Shift(val dx: Int, val dy: Int)
