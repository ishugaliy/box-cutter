package it.devchallenge

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.jackson.jackson
import io.ktor.server.netty.EngineMain
import it.devchallenge.api.configureErrorHandling
import it.devchallenge.api.configureRouting

fun main(args: Array<String>): Unit = EngineMain.main(args)

fun Application.main() {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }
    configureRouting()
    configureErrorHandling()
}
