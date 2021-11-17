package it.devchallenge

import java.awt.Point

data class Box(val w: Int, val d: Int, val h: Int) {
    fun unfoldedWidth() = 2 * (w + h)
    fun unfoldedHeight() = 2 * h + d

    fun createContour(x: Int, y: Int) = BoxContour(this, Point(x, y))
}

data class BoxContour(
    val box: Box,
    val position: Point
) {
    private val path = with(box) {
        listOf(
            Point(h, 0),
            Point(h, h),
            Point(0, h),
            Point(0, h + d),
            Point(h, h + d),
            Point(h, 2 * h + d),
            Point(h + w, 2 * h + d),
            Point(h + w, h + d),
            Point(2 * (h + w), h + d),
            Point(2 * (h + w), h),
            Point(h + w, h),
            Point(h + w, 0),
            Point(h, 0)
        ).onEach { it.translate(position.x, position.y) }
    }

    fun path() = path
}
