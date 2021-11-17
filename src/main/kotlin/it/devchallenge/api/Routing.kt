package it.devchallenge.api

import io.ktor.application.Application
import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.StatusPages
import io.ktor.http.HttpStatusCode.Companion.UnprocessableEntity
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.Routing
import io.ktor.routing.post
import io.ktor.routing.routing
import it.devchallenge.api.CuttingProgramResponse.CommandDto
import it.devchallenge.cnc.CNCMachine
import org.slf4j.LoggerFactory
import org.slf4j.LoggerFactory.getLogger

fun Application.configureRouting() {
    install(Routing) {
        routing {
            post(path = "/api/simple_box") {
                val cuttingRequest = call.receive<CuttingProgramRequest>().validate()
                val cnc = CNCMachine(
                    box = cuttingRequest.box(),
                    sheet = cuttingRequest.sheet()
                )
                val program = cnc.program()
                call.respond(
                    CuttingProgramResponse(
                        success = true,
                        amount = program.amount,
                        program = program.commands.map(::CommandDto)
                    )
                )
            }
        }
    }
}

fun Application.configureErrorHandling() {
    install(StatusPages) {
        exception<IllegalArgumentException> { call.respondError(ValidationErrorResponse(it.message)) }
        exception<Throwable> {
            call.respondError(InternalErrorResponse(it))
            getLogger(Application::class.java).error(it.message, it)
        }
    }
}

private suspend fun ApplicationCall.respondError(response: CuttingProgramResponse) =
    respond(UnprocessableEntity, response)
