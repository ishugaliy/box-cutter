package it.devchallenge;

import java.awt.Point

data class Sheet(val w: Int, val l: Int) {
    fun isOnEdge(a: Point, b: Point) =
        ((a.x == 0 || a.x == w) && a.x == b.x) || ((a.y == 0 || a.y == l) && a.y == b.y)
}
