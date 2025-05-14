package com.airalo.example.offer.data.repository

import com.airalo.example.offer.api.client.CountriesApi
import com.airalo.example.offer.api.model.CountryDTO
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.entity.CountryFlagUri
import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.offer.domain.repository.OfferRepositoryContract
import com.airalo.example.test.mockclient.Content
import com.airalo.example.test.mockclient.createMockClient
import com.goncalossilva.resources.Resource
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.openapitools.client.infrastructure.ApiClient
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

class OfferRepositorySpec {
    private val serializedCountries = Resource("src/commonTest/resources/Countries.json").readText()
    private val countries = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }.decodeFromString(
        ListSerializer(CountryDTO.serializer()),
        serializedCountries,
    )

    @Test
    fun `When listPopularCountries is called Then it returns an empty list if the API end point fails`() =
        runTest {
            // Arrange
            val expectedError = Error(serializedCountries)
            val apiEndpoint = CountriesApi(
                baseUrl = ApiClient.BASE_URL,
                httpClient = createMockClient(
                    Content(""),
                    expectedError,
                    HttpStatusCode.NotAcceptable,
                ),
            )

            val repository = OfferRepository(apiEndpoint)

            // Act
            val countries = repository.listPopularCountries()

            // Assert
            countries mustBe emptyList()
        }

    @Test
    fun `When listPopularCountries is called Then it returns the list of countries provided by the API point`() =
        runTest {
            // Arrange
            val apiEndpoint = CountriesApi(
                baseUrl = ApiClient.BASE_URL,
                httpClient = createMockClient(
                    Content(serializedCountries, mapOf("Content-Type" to listOf("application/json"))),
                    null,
                    HttpStatusCode.OK,
                ) {
                    url.encodedQuery mustBe "type=popular"
                }.config {
                    install(ContentNegotiation) {
                        json(ApiClient.JSON_DEFAULT)
                    }
                },
            )

            val repository = OfferRepository(apiEndpoint)

            // Act
            val countries = repository.listPopularCountries()

            // Assert
            countries mustBe this@OfferRepositorySpec.countries.map {
                Country(
                    id = Id(it.id),
                    flag = CountryFlagUri(it.image.url),
                    name = it.title,
                )
            }
        }

    @Test
    fun `It fulfils OfferRepositoryContract`() {
        OfferRepository(CountriesApi(baseUrl = ApiClient.BASE_URL, httpClient = createMockClient(Content("")))) fulfils OfferRepositoryContract::class
    }
}
