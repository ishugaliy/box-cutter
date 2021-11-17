package it.devchallenge.api;

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.application.Application
import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpHeaders.Accept
import io.ktor.http.HttpHeaders.ContentType
import io.ktor.http.HttpMethod.Companion.Post
import io.ktor.http.HttpStatusCode
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.http.HttpStatusCode.Companion.UnprocessableEntity
import io.ktor.server.testing.*
import it.devchallenge.CuttingProgramTestScenariosFactory
import it.devchallenge.api.CuttingProgramRequest.BoxSizeDto
import it.devchallenge.api.CuttingProgramRequest.SheetSizeDto
import it.devchallenge.main
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RoutingTest {
    private val endpoint = "/api/simple_box"

    @Test
    fun `should return expected program to all test scenarios`() =
        withTestApplication(Application::main) {
            CuttingProgramTestScenariosFactory
                .boxTestScenarios()
                .forEach { testCase ->
                    val testProgramRequest = CuttingProgramRequest(testCase.sheetSize, testCase.boxSize)

                    val response = postCall<CuttingProgramResponse>(endpoint, testProgramRequest)

                    assertNotNull(response)
                    assertTrue(response.success!!)
                    assertNull(response.error)
                    assertEquals(testCase.expectedAmount, response.amount)
                    assertNotNull(response.program)
                    assertEquals(testCase.expectedProgram.size, response.program?.size)
                    testCase.expectedProgram.forEachIndexed { idx, cmd ->
                        assertEquals(cmd.type, response.program?.get(idx)?.command)
                        assertEquals(cmd.point?.x, response.program?.get(idx)?.x)
                        assertEquals(cmd.point?.y, response.program?.get(idx)?.y)
                    }
                }
        }

    @Test
    fun `box size bigger than sheet size - should return 422 validation error`() {
        withTestApplication(Application::main) {
            val response = postCall<CuttingProgramResponse>(
                endpoint = endpoint,
                body = CuttingProgramRequest(
                    sheetSize = SheetSizeDto(w = 200, l = 400),
                    boxSize = BoxSizeDto(w = 100, d = 100, h = 100)),
                expectedStatus = UnprocessableEntity
            ).also { assertErrorResponse(it) }
        }
    }

    @Test
    fun `sheet size has zero and negative values - 422 validation error`() {
        withTestApplication(Application::main) {
            val response = postCall<CuttingProgramResponse>(
                endpoint = endpoint,
                body = CuttingProgramRequest(
                    sheetSize = SheetSizeDto(w = 0, l = -600),
                    boxSize = BoxSizeDto(w = 100, d = 100, h = 100)),
                expectedStatus = UnprocessableEntity
            ).also { assertErrorResponse(it) }
        }
    }

    @Test
    fun `box size has zero and negative values - 422 validation error`() {
        withTestApplication(Application::main) {
            val response = postCall<CuttingProgramResponse>(
                endpoint = endpoint,
                body = CuttingProgramRequest(
                    sheetSize = SheetSizeDto(w = 600, l = 800),
                    boxSize = BoxSizeDto(w = 100, d = 0, h = -1)),
                expectedStatus = UnprocessableEntity
            ).also { assertErrorResponse(it) }
        }
    }

    @Test
    fun `sheet size was not set - 422 validation error`() {
        withTestApplication(Application::main) {
            val response = postCall<CuttingProgramResponse>(
                endpoint = endpoint,
                body = CuttingProgramRequest(boxSize = BoxSizeDto(1, 1)),
                expectedStatus = UnprocessableEntity
            ).also { assertErrorResponse(it) }
        }
    }

    @Test
    fun `box size was not set - 422 validation error`() {
        withTestApplication(Application::main) {
            postCall<CuttingProgramResponse>(
                endpoint = endpoint,
                body = CuttingProgramRequest(sheetSize = SheetSizeDto(1, 1)),
                expectedStatus = UnprocessableEntity
            ).also { assertErrorResponse(it) }
        }
    }
}

private inline fun <reified V> TestApplicationEngine.postCall(endpoint: String, body: Any, expectedStatus: HttpStatusCode = OK): V {
    val mapper =  ObjectMapper()
    val response = handleRequest(Post, endpoint) {
        addHeader(ContentType, Json.toString())
        addHeader(Accept, Json.toString())
        setBody(ObjectMapper().writeValueAsString(body))
    }.response
    assertEquals(expectedStatus, response.status())
    return mapper.readValue(response.content, V::class.java)
}

private fun assertErrorResponse(response: CuttingProgramResponse) {
    assertNotNull(response)
    assertFalse(response.success!!)
    assertNotNull(response.error)
    assertNull(response.amount)
    assertNull(response.program)
}


