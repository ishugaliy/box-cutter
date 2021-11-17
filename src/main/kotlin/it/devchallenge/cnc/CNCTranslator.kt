package it.devchallenge.cnc

import it.devchallenge.Sheet
import it.devchallenge.cnc.CommandType.DOWN
import it.devchallenge.cnc.CommandType.GOTO
import it.devchallenge.cnc.CommandType.START
import it.devchallenge.cnc.CommandType.STOP
import it.devchallenge.cnc.CommandType.UP
import java.awt.Point

data class CNCTranslator(
    val sheet: Sheet,
    val patterns: List<BoxPattern>
) {
    fun commands(): List<Command> {
        val cmds = mutableListOf<Command>()

        cmds += Command(START)
        for (pattern in patterns) {
            val path = pattern.contourPath()
            cmds += gotoIdle(path.first())
            for (i in 0 until path.size - 1) {
                val curr = path[i]
                val next = path[i + 1]
                if (sheet.isEdge(curr, next)) {
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
