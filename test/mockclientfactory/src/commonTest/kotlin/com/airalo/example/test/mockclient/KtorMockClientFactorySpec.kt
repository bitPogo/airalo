package com.airalo.example.test.mockclient

import io.ktor.client.call.body
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.head
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlinx.coroutines.test.runTest
import tech.antibytes.kfixture.PublicApi
import tech.antibytes.kfixture.fixture
import tech.antibytes.kfixture.kotlinFixture
import tech.antibytes.kfixture.qualifier.qualifiedBy

@Suppress("ktlint:standard:max-line-length")
class KtorMockClientFactorySpec {
    private val fixture = kotlinFixture {
        addGenerator(
            String::class,
            StringAlphaGenerator,
            qualifiedBy("alpha"),
        )
    }

    @Test
    fun `When createMockClient is called with a String it returns a HttpClient which responds with the given String`() =
        runTest {
            // Given
            val message: String = fixture.fixture()

            // When
            val client = createMockClient(message)

            val response1: String = client.get(fixture.fixture<String>(qualifiedBy("alpha"))).body()
            val response2: String = client.post(fixture.fixture<String>(qualifiedBy("alpha"))).body()
            val response3: String = client.put(fixture.fixture<String>(qualifiedBy("alpha"))).body()
            val response4: String = client.delete(fixture.fixture<String>(qualifiedBy("alpha"))).body()
            val response5: String = client.patch(fixture.fixture<String>(qualifiedBy("alpha"))).body()
            val response6: String = client.head(fixture.fixture<String>(qualifiedBy("alpha"))).body()

            // Then
            assertEquals(
                actual = response1,
                expected = message,
            )
            assertEquals(
                actual = response2,
                expected = message,
            )
            assertEquals(
                actual = response3,
                expected = message,
            )
            assertEquals(
                actual = response4,
                expected = message,
            )
            assertEquals(
                actual = response5,
                expected = message,
            )
            assertEquals(
                actual = response6,
                expected = message,
            )
        }

    @Test
    fun `When createMockClient is called with Content it returns a HttpClient which respondes with the given parameter`() =
        runTest {
            // Given
            val accept = "Plain/text"
            val content = Content(
                fixture.fixture(),
                mapOf("Accept" to listOf(accept)),
            )

            // When
            val client = createMockClient(content)

            val response1: HttpResponse = client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            val response2: HttpResponse = client.post(fixture.fixture<String>(qualifiedBy("alpha")))
            val response3: HttpResponse = client.put(fixture.fixture<String>(qualifiedBy("alpha")))
            val response4: HttpResponse = client.delete(fixture.fixture<String>(qualifiedBy("alpha")))
            val response5: HttpResponse = client.patch(fixture.fixture<String>(qualifiedBy("alpha")))
            val response6: HttpResponse = client.head(fixture.fixture<String>(qualifiedBy("alpha")))

            // Then
            assertEquals(
                actual = response1.headers["Accept"],
                expected = accept,
            )
            assertEquals(
                actual = response2.headers["Accept"],
                expected = accept,
            )
            assertEquals(
                actual = response3.headers["Accept"],
                expected = accept,
            )
            assertEquals(
                actual = response4.headers["Accept"],
                expected = accept,
            )
            assertEquals(
                actual = response5.headers["Accept"],
                expected = accept,
            )
            assertEquals(
                actual = response6.headers["Accept"],
                expected = accept,
            )
        }

    @Test
    fun `When createMockClient is called with a String and a StatusCode which is in 2xx it returns a HttpClient which respondes with the given StatusCode`() =
        runTest {
            // Given
            val status = HttpStatusCode.Created

            // When
            val client = createMockClient(
                fixture.fixture<String>(),
                status = status,
            )

            val response1: HttpResponse = client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            val response2: HttpResponse = client.post(fixture.fixture<String>(qualifiedBy("alpha")))
            val response3: HttpResponse = client.put(fixture.fixture<String>(qualifiedBy("alpha")))
            val response4: HttpResponse = client.delete(fixture.fixture<String>(qualifiedBy("alpha")))
            val response5: HttpResponse = client.patch(fixture.fixture<String>(qualifiedBy("alpha")))
            val response6: HttpResponse = client.head(fixture.fixture<String>(qualifiedBy("alpha")))

            // Then
            assertEquals(
                actual = response1.status,
                expected = status,
            )
            assertEquals(
                actual = response2.status,
                expected = status,
            )
            assertEquals(
                actual = response3.status,
                expected = status,
            )
            assertEquals(
                actual = response4.status,
                expected = status,
            )
            assertEquals(
                actual = response5.status,
                expected = status,
            )
            assertEquals(
                actual = response6.status,
                expected = status,
            )
        }

    @Test
    fun `When createMockClient is called with a String and a StatusCode and Content which is in 2xx it returns a HttpClient which respondes with the given StatusCode`() =
        runTest {
            // Given
            val accept = "Plain/text"
            val content = Content(
                fixture.fixture(),
                mapOf("Accept" to listOf(accept)),
            )
            val status = HttpStatusCode.Created

            // When
            val client = createMockClient(
                content,
                status = status,
            )

            val response1: HttpResponse = client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            val response2: HttpResponse = client.post(fixture.fixture<String>(qualifiedBy("alpha")))
            val response3: HttpResponse = client.put(fixture.fixture<String>(qualifiedBy("alpha")))
            val response4: HttpResponse = client.delete(fixture.fixture<String>(qualifiedBy("alpha")))
            val response5: HttpResponse = client.patch(fixture.fixture<String>(qualifiedBy("alpha")))
            val response6: HttpResponse = client.head(fixture.fixture<String>(qualifiedBy("alpha")))

            // Then
            assertEquals(
                actual = response1.status,
                expected = status,
            )
            assertEquals(
                actual = response2.status,
                expected = status,
            )
            assertEquals(
                actual = response3.status,
                expected = status,
            )
            assertEquals(
                actual = response4.status,
                expected = status,
            )
            assertEquals(
                actual = response5.status,
                expected = status,
            )
            assertEquals(
                actual = response6.status,
                expected = status,
            )
        }

    @Test
    fun `When createMockClient is called with a assertion it returns an assertable MockClient`() =
        runTest {
            // Given
            val host1: String = fixture.fixture()
            val host2: String = fixture.fixture()

            // When
            val client = createMockClient(
                fixture.fixture<String>(),
            ) {
                // Then
                assertEquals(
                    actual = url.fullPath,
                    expected = "/$host1",
                )
            }

            client.get(host1)
            client.post(host1)
            client.put(host1)
            client.delete(host1)
            client.patch(host1)
            client.head(host1)

            assertFailsWith<AssertionError> {
                client.get(host2)
                client.post(host2)
                client.put(host2)
                client.delete(host2)
                client.patch(host2)
                client.head(host2)
            }
        }

    @Test
    fun `When createMockClient is called with a assertion and Content it returns an assertable MockClient`() =
        runTest {
            // Given
            val accept = "Plain/text"
            val content = Content(
                fixture.fixture(),
                mapOf("Accept" to listOf(accept)),
            )
            val host1: String = fixture.fixture()
            val host2: String = fixture.fixture()

            // When
            val client = createMockClient(content) {
                // Then
                assertEquals(
                    actual = url.fullPath,
                    expected = "/$host1",
                )
            }

            client.get(host1)
            client.post(host1)
            client.put(host1)
            client.delete(host1)
            client.patch(host1)
            client.head(host1)

            assertFailsWith<AssertionError> {
                client.get(host2)
                client.post(host2)
                client.put(host2)
                client.delete(host2)
                client.patch(host2)
                client.head(host2)
            }
        }

    @Test
    fun `When createMockClient is called with a String a Throwable and a StatusCode which is not 2xx it returns a HttpClient which throws the given Exception`() =
        runTest {
            // Given
            val status = HttpStatusCode.NotFound
            val error = RuntimeException(fixture.fixture<String>())

            // When
            val client = createMockClient(
                fixture.fixture<String>(),
                error = error,
                status = status,
            )

            val exception1 = assertFailsWith<RuntimeException> {
                client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            }
            val exception2 = assertFailsWith<RuntimeException> {
                client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            }
            val exception3 = assertFailsWith<RuntimeException> {
                client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            }
            val exception4 = assertFailsWith<RuntimeException> {
                client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            }
            val exception5 = assertFailsWith<RuntimeException> {
                client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            }
            val exception6 = assertFailsWith<RuntimeException> {
                client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            }

            // Then
            assertEquals(
                actual = exception1.message!!,
                expected = error.message!!,
            )
            assertEquals(
                actual = exception2.message!!,
                expected = error.message!!,
            )
            assertEquals(
                actual = exception3.message!!,
                expected = error.message!!,
            )
            assertEquals(
                actual = exception4.message!!,
                expected = error.message!!,
            )
            assertEquals(
                actual = exception5.message!!,
                expected = error.message!!,
            )
            assertEquals(
                actual = exception6.message!!,
                expected = error.message!!,
            )
        }

    @Test
    fun `When createMockClient is called with Content a Throwable and a StatusCode which is not 2xx it returns a HttpClient which throws the given Exception`() =
        runTest {
            // Given
            val accept = "Plain/text"
            val content = Content(
                fixture.fixture(),
                mapOf("Accept" to listOf(accept)),
            )
            val status = HttpStatusCode.NotFound
            val error = RuntimeException(fixture.fixture<String>())

            // When
            val client = createMockClient(
                content,
                error = error,
                status = status,
            )

            val exception1 = assertFailsWith<RuntimeException> {
                client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            }
            val exception2 = assertFailsWith<RuntimeException> {
                client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            }
            val exception3 = assertFailsWith<RuntimeException> {
                client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            }
            val exception4 = assertFailsWith<RuntimeException> {
                client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            }
            val exception5 = assertFailsWith<RuntimeException> {
                client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            }
            val exception6 = assertFailsWith<RuntimeException> {
                client.get(fixture.fixture<String>(qualifiedBy("alpha")))
            }

            // Then
            assertEquals(
                actual = exception1.message!!,
                expected = error.message!!,
            )
            assertEquals(
                actual = exception2.message!!,
                expected = error.message!!,
            )
            assertEquals(
                actual = exception3.message!!,
                expected = error.message!!,
            )
            assertEquals(
                actual = exception4.message!!,
                expected = error.message!!,
            )
            assertEquals(
                actual = exception5.message!!,
                expected = error.message!!,
            )
            assertEquals(
                actual = exception6.message!!,
                expected = error.message!!,
            )
        }
}

internal class StringAlphaGenerator(
    private val random: Random,
) : PublicApi.Generator<String> {
    override fun generate(): String {
        val length = random.nextInt(1, 10)
        val chars = ByteArray(length)

        for (idx in 0 until length) {
            val char = random.nextInt(65, 91)
            chars[idx] = if (random.nextBoolean()) {
                (char + 32).toByte()
            } else {
                char.toByte()
            }
        }

        return chars.decodeToString()
    }

    companion object : PublicApi.GeneratorFactory<String> {
        override fun getInstance(random: Random): PublicApi.Generator<String> {
            return StringAlphaGenerator(random)
        }
    }
}
