package it.devchallenge

import it.devchallenge.api.CuttingProgramRequest.BoxSizeDto
import it.devchallenge.api.CuttingProgramRequest.SheetSizeDto
import it.devchallenge.cnc.Command
import it.devchallenge.cnc.CommandType.DOWN
import it.devchallenge.cnc.CommandType.GOTO
import it.devchallenge.cnc.CommandType.START
import it.devchallenge.cnc.CommandType.STOP
import it.devchallenge.cnc.CommandType.UP

object CuttingProgramTestScenariosFactory {
    fun testScenarios() = listOf(singlePatternTestScenario, fourPatternsTestScenario)

    private val singlePatternTestScenario = CuttingProgramTestScenario(
        sheetSize = SheetSizeDto(w = 800, l = 600),
        boxSize = BoxSizeDto(w = 200, d = 200, h = 200),
        expectedAmount = 1,
        expectedProgram = listOf(
            Command(START),

            Command(UP), Command(GOTO, 200, 0), Command(DOWN), Command(GOTO, 200, 200),
            Command(GOTO, 0, 200), Command(UP), Command(GOTO, 0, 400), Command(DOWN),
            Command(GOTO, 200, 400), Command(GOTO, 200, 600), Command(UP), Command(GOTO, 400, 600),
            Command(DOWN), Command(GOTO, 400, 400), Command(GOTO, 800, 400), Command(UP),
            Command(GOTO, 800, 200), Command(DOWN), Command(GOTO, 400, 200), Command(GOTO, 400, 0),
            Command(UP), Command(GOTO, 200, 0), Command(DOWN),

            Command(STOP)
        )
    )

    private val fourPatternsTestScenario = CuttingProgramTestScenario(
        sheetSize = SheetSizeDto(w = 2000, l = 1200),
        boxSize = BoxSizeDto(w = 200, d = 200, h = 200),
        expectedAmount = 4,
        expectedProgram = listOf(
            Command(START),

            // First pattern
            Command(UP), Command(GOTO, 200, 0), Command(DOWN),
            Command(GOTO, 200, 200), Command(GOTO, 0, 200),
            Command(UP), Command(GOTO, 0, 400), Command(DOWN),
            Command(GOTO, 200, 400), Command(GOTO, 200, 600), Command(GOTO, 400, 600),
            Command(GOTO, 400, 400), Command(GOTO, 800, 400), Command(GOTO, 800, 200),
            Command(GOTO, 400, 200), Command(GOTO, 400, 0),
            Command(UP), Command(GOTO, 200, 0), Command(DOWN),

            // Seconds pattern
            Command(UP), Command(GOTO, 1000, 0), Command(DOWN),
            Command(GOTO, 1000, 200), Command(GOTO, 800, 200), Command(GOTO, 800, 400),
            Command(GOTO, 1000, 400), Command(GOTO, 1000, 600), Command(GOTO, 1200, 600),
            Command(GOTO, 1200, 400), Command(GOTO, 1600, 400), Command(GOTO, 1600, 200),
            Command(GOTO, 1200, 200), Command(GOTO, 1200, 0),
            Command(UP), Command(GOTO, 1000, 0), Command(DOWN),

            // Third pattern
            Command(UP), Command(GOTO, 400, 400), Command(DOWN),
            Command(GOTO, 400, 600), Command(GOTO, 200, 600), Command(GOTO, 200, 800),
            Command(GOTO, 400, 800), Command(GOTO, 400, 1000), Command(GOTO, 600, 1000),
            Command(GOTO, 600, 800), Command(GOTO, 1000, 800), Command(GOTO, 1000, 600),
            Command(GOTO, 600, 600), Command(GOTO, 600, 400), Command(GOTO, 400, 400),

            // Fourth pattern
            Command(UP), Command(GOTO, 1200, 400), Command(DOWN),
            Command(GOTO, 1200, 600),
            Command(GOTO, 1000, 600), Command(GOTO, 1000, 800), Command(GOTO, 1200, 800),
            Command(GOTO, 1200, 1000), Command(GOTO, 1400, 1000), Command(GOTO, 1400, 800),
            Command(GOTO, 1800, 800), Command(GOTO, 1800, 600), Command(GOTO, 1400, 600),
            Command(GOTO, 1400, 400), Command(GOTO, 1200, 400),

            Command(STOP)
        )
    )
}

data class CuttingProgramTestScenario(
    val boxSize: BoxSizeDto,
    val sheetSize: SheetSizeDto,
    val expectedAmount: Int,
    val expectedProgram: List<Command>
)
