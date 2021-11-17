package it.devchallenge.cnc

import it.devchallenge.Box
import it.devchallenge.BoxContour
import it.devchallenge.Sheet

data class CNCProgram(val amount: Int, val commands: List<Command>)

/*
Fill sheet layout with box contours and return specific program to execute.
 */
class CNCMachine(
    private val box: Box,
    private val sheet: Sheet
) {
    fun program(): CNCProgram {
        val contours = mutableListOf<BoxContour>()
        var level = 0
        var floor = 0
        while (sheet.l - floor >= box.unfoldedHeight()) {
            if (useShiftedLayout()) {
                val dx = if (level++ % 2 != 0) box.h else 0
                contours += placeContours(on = floor, dx = dx)
                floor += box.unfoldedHeight() - box.h
            } else {
                contours += placeContours(on = floor)
                floor += box.unfoldedHeight()
            }
        }
        return CNCProgram(
            amount = contours.size,
            commands = CNCTranslator(sheet, contours).commands()
        )
    }

    private fun placeContours(on: Int, dx: Int = 0): List<BoxContour> {
        val contours = mutableListOf<BoxContour>()
        repeat(lineSize()) { col ->
            contours += box.createContour(
                x = box.unfoldedWidth() * col + dx,
                y = on
            )
        }
        return contours
    }

    private fun useShiftedLayout() = sheet.w - lineSize() * box.unfoldedWidth() >= box.h
    private fun lineSize() = sheet.w / box.unfoldedWidth()
}
