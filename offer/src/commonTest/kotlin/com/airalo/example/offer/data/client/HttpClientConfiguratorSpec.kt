package com.airalo.example.offer.data.client

import com.airalo.example.test.mockclient.Content
import com.airalo.example.test.mockclient.createMockClient
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.HttpClientEngineCapability
import io.ktor.client.engine.HttpClientEngineFactory
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.MockEngineConfig
import io.ktor.client.engine.mock.MockRequestHandleScope
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.request.HttpRequestData
import io.ktor.client.request.HttpResponseData
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.http.HttpStatusCode
import io.ktor.utils.io.InternalAPI
import kotlin.coroutines.CoroutineContext
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

class HttpClientConfiguratorSpec {
    @OptIn(ExperimentalSerializationApi::class)
    private val json = Json {
        allowTrailingComma = true
        ignoreUnknownKeys = true
        isLenient = true
    }

    @Test
    fun `When configure is called it Then it sets up JSON parsing`() =
        runTest {
            // Given
            val value = "value"
            val rawClient = createMockClient(
                Content("{key: $value, unknown: pair,}", mapOf("Content-Type" to listOf("application/json"))),
            )

            // When
            val response: TestJson = HttpClientConfigurator(
                json,
            ).configure(rawClient).get("json").body()

            // Then
            assertEquals(
                actual = response,
                expected = TestJson(value),
            )
        }

    @Test
    fun `When configure is called Then it setups up a request timeout`() =
        runTest {
            // Given
            val callTime = mutableListOf(299999L, 300001L)

            val rawClient = HttpClient(ConfigurableMockEngineFactory(StandardTestDispatcher(testScheduler))) {
                engine {
                    addHandler { _ ->
                        delay(callTime.removeAt(0))
                        respond(
                            content = "",
                            status = HttpStatusCode.OK,
                        )
                    }
                }
            }

            // When
            assertTrue {
                HttpClientConfigurator(
                    json,
                ).configure(rawClient).post("https://www.google.com/").body<String>().isEmpty()
            }

            assertFailsWith<HttpRequestTimeoutException> {
                HttpClientConfigurator(
                    json,
                ).configure(rawClient).post("https://www.google.com/").body<String>()
            }
        }
}

@Serializable
private data class TestJson(val key: String)

private class ConfigurableMockEngine(
    override val config: MockEngineConfig,
    override val dispatcher: CoroutineDispatcher,
) : HttpClientEngine {
    private val mockEngine: MockEngine = MockEngine(config)

    override val coroutineContext: CoroutineContext
        get() = mockEngine.coroutineContext + dispatcher

    @Suppress("KDocMissingDocumentation")
    override fun close() = mockEngine.close()

    @InternalAPI
    override suspend fun execute(data: HttpRequestData): HttpResponseData = mockEngine.execute(data)

    override val supportedCapabilities: Set<HttpClientEngineCapability<out Any>> = mockEngine.supportedCapabilities
}

private class ConfigurableMockEngineFactory(
    val dispatcher: CoroutineDispatcher,
) : HttpClientEngineFactory<MockEngineConfig> {
    override fun create(block: MockEngineConfig.() -> Unit): HttpClientEngine = ConfigurableMockEngine(MockEngineConfig().apply(block), dispatcher)

    operator fun invoke(handler: suspend MockRequestHandleScope.(HttpRequestData) -> HttpResponseData): ConfigurableMockEngine =
        ConfigurableMockEngine(
            MockEngineConfig().apply {
                requestHandlers.add(handler)
            },
            dispatcher,
        )
}
