package it.devchallenge.cnc

import it.devchallenge.Box
import it.devchallenge.Sheet

class CNCMachine(
    private val box: Box,
    private val sheet: Sheet
) {
    fun program(): CNCProgram {
        val rowCount = sheet.w / box.unfoldedWidth()
        val colCount = sheet.l / box.unfoldedHeight()
        val patterns = mutableListOf<BoxPattern>()
        repeat(rowCount) { row ->
            repeat(colCount) { col ->
                patterns += BoxPattern(box).apply {
                    shift(
                        dx = width() * row,
                        dy = height() * col
                    )
                }
            }
        }
        return CNCProgram(
            amount = patterns.size,
            commands = CNCTranslator(sheet, patterns).commands()
        )
    }
}

data class CNCProgram(val amount: Int, val commands: List<Command>)
