package it.devchallenge.api

import it.devchallenge.Box
import it.devchallenge.Sheet

data class CuttingProgramRequest(
    val sheetSize: SheetSizeDto? = null,
    val boxSize: BoxSizeDto? = null
) {
    data class SheetSizeDto(val w: Int? = null, val l: Int? = null)
    data class BoxSizeDto(val w: Int? = null, val d: Int? = null, val h: Int? = null)

    fun validate(): CuttingProgramRequest {
        notNullableConstraint("Sheet Size", sheetSize)
        positiveConstraint("Sheet Width", sheetSize?.w)
        positiveConstraint("Sheet Length", sheetSize?.l)

        notNullableConstraint("Box size", boxSize)
        positiveConstraint("Box Width", boxSize?.w)
        positiveConstraint("Box Depth", boxSize?.d)
        positiveConstraint("Box Height", boxSize?.h)

        constraint("Sheet size is too small to cut single box") { box().unfoldedWidth() > sheet().w }

        return this
    }

    fun box() = Box(w = boxSize?.w!!, d = boxSize.d!!, h = boxSize.h!!)
    fun sheet() = Sheet(w = sheetSize?.w!!, l = sheetSize.l!!)
}
