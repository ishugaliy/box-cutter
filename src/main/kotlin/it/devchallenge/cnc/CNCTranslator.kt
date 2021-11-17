package it.devchallenge.cnc

import it.devchallenge.BoxContour
import it.devchallenge.Sheet
import it.devchallenge.cnc.CommandType.DOWN
import it.devchallenge.cnc.CommandType.GOTO
import it.devchallenge.cnc.CommandType.START
import it.devchallenge.cnc.CommandType.STOP
import it.devchallenge.cnc.CommandType.UP
import java.awt.Point

/*
Responsible for converting box contours into the list of CNC commands.
 */
data class CNCTranslator(
    val sheet: Sheet,
    val contours: List<BoxContour>
) {
    fun commands(): List<Command> {
        val cmds = mutableListOf<Command>()

        cmds += Command(START)
        for (contour in contours) {
            val path = contour.path()
            cmds += gotoIdle(path.first())
            for (i in 0 until path.size - 1) {
                val curr = path[i]
                val next = path[i + 1]
                if (sheet.isOnEdge(curr, next)) {
                    cmds += gotoIdle(next)
                } else {
                    cmds += Command(GOTO, next)
                }
            }
        }
        cmds += Command(STOP)

        return cmds
    }

    private fun gotoIdle(point: Point) = listOf(
        Command(UP),
        Command(GOTO, point),
        Command(DOWN)
    )
}

data class Command(val type: CommandType, val point: Point? = null) {
    constructor(type: CommandType, x: Int, y: Int) : this(type, Point(x, y))
}
enum class CommandType { START, DOWN, UP, GOTO, STOP }
