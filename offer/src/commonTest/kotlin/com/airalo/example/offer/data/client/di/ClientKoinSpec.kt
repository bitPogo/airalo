package com.airalo.example.offer.data.client.di

import io.ktor.client.HttpClient
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.koin.core.qualifier.named
import org.koin.dsl.koinApplication
import tech.antibytes.util.test.mustBe

class ClientKoinSpec {
    @Test
    fun `It resolves an configured HttpClient`() {
        val koin = koinApplication {
            modules(
                resolveClientKoin(),
            )
        }

        // When
        val client: HttpClient? = koin.koin.getOrNull()

        // Then
        assertNotNull(client)
    }

    @Test
    fun `It resolves a Json (De)Serializer`() {
        val koin = koinApplication {
            modules(
                resolveClientKoin(),
            )
        }

        // When
        val json: Json? = koin.koin.getOrNull()

        // Then
        assertNotNull(json)
        assertEquals(
            actual = json.decodeFromString(TestJson.serializer(), "{key: value, unknown: pair}"),
            expected = TestJson("value"),
        )
    }

    @Test
    fun `It contains a BaseUrl`() {
        val koin = koinApplication {
            modules(
                resolveClientKoin(),
            )
        }

        // When
        val baseUrl: String? = koin.koin.getOrNull(named("BaseUrl"))

        // Then
        baseUrl mustBe "https://www.airalo.com/api/v2/"
    }
}

@Serializable
private data class TestJson(
    val key: String,
)
