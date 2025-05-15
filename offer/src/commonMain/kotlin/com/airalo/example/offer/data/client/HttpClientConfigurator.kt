package com.airalo.example.offer.data.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

/**
 * Configures a [HttpClient] instance.
 */
internal class HttpClientConfigurator(
    private val json: Json,
) {
    private fun HttpClientConfig<*>.enableFailingOnNon2xxStatus() {
        expectSuccess = true
    }

    private fun HttpClientConfig<*>.configureJson() {
        install(ContentNegotiation) {
            json(json)
        }
    }

    private fun HttpClientConfig<*>.configureTimeout() {
        install(HttpTimeout) {
            requestTimeoutMillis = 300000
        }
    }

    /**
     * Configures the provided [HttpClient] with:
     *   - JSON Parsing
     *   - Failing on Non-2xx Status
     *   - Timeout
     *
     * @param client The [HttpClient] instance to be configured.
     * @return The configured [HttpClient] instance.
     */
    fun configure(client: HttpClient): HttpClient {
        return client.config {
            enableFailingOnNon2xxStatus()
            configureJson()
            configureTimeout()
        }
    }
}
