package com.airalo.example.test.mockclient

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.http.isSuccess

// Taken from: https://github.com/bitPogo/test-utils-kmp/blob/86ec4d671257c449f273350d46614ea4b4ad82b4/test-utils-ktor/src/commonMain/kotlin/tech/antibytes/util/test/ktor/KtorMockClientFactory.kt
data class Content(
    val content: String,
    val headers: Map<String, List<String>> = emptyMap(),
)

private fun addResponse(
    scope: MockRequestHandleScope,
    response: String,
    headers: Map<String, List<String>>,
    status: HttpStatusCode,
): HttpResponseData {
    val requestHeaders = headers {
        headers.forEach { (name, value) ->
            appendAll(name, value)
        }
    }

    return if (status.isSuccess()) {
        scope.respond(
            content = response,
            status = status,
            headers = requestHeaders,
        )
    } else {
        scope.respondError(status = status)
    }
}

fun createMockClient(
    response: Content,
    error: Throwable? = null,
    status: HttpStatusCode = HttpStatusCode.OK,
    assert: suspend HttpRequestData.() -> Unit = {},
): HttpClient {
    return HttpClient(MockEngine) {
        engine {
            addHandler {
                assert(it)
                addResponse(
                    this,
                    response = response.content,
                    status = status,
                    headers = response.headers,
                )
            }
        }

        if (!status.isSuccess()) {
            expectSuccess = true
            HttpResponseValidator {
                handleResponseExceptionWithRequest { _, _ ->
                    throw error!!
                }
            }
        }
    }
}

fun createMockClient(
    response: String,
    error: Throwable? = null,
    status: HttpStatusCode = HttpStatusCode.OK,
    assert: HttpRequestData.() -> Unit = {},
): HttpClient = createMockClient(Content(response), error, status, assert)
