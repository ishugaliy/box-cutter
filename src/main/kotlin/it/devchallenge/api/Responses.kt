package it.devchallenge.api

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL
import it.devchallenge.cnc.Command
import it.devchallenge.cnc.CommandType

@JsonInclude(NON_NULL)
open class CuttingProgramResponse(
    val success: Boolean? = null,
    val error: String? = null,
    val amount: Int? = null,
    val program: List<CommandDto>? = null
) {
    @JsonInclude(NON_NULL)
    data class CommandDto(
        val command: CommandType? = null,
        val x: Int? = null,
        val y: Int? = null
    ) {
        constructor(command: Command) : this(
            command = command.type,
            x = command.point?.x,
            y = command.point?.y
        )
    }
}

class ValidationErrorResponse(message: String?) : CuttingProgramResponse(
    success = false,
    error = "Invalid input format. Please check: [$message]"
)

class InternalErrorResponse(e: Throwable) : CuttingProgramResponse(
    success = false,
    error = "Internal service error. Please, check your input or contact your service provider. Cause [${e.message}]"
)
