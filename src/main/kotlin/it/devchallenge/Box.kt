package it.devchallenge

data class Box(val w: Int, val d: Int, val h: Int) {
    fun unfoldedWidth() = 2 * (w + h)
    fun unfoldedHeight() = 2 * h + d
}
