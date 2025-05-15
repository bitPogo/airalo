package com.airalo.example.offer.data.repository

import com.airalo.example.offer.api.client.CountriesApi
import com.airalo.example.offer.api.client.OffersInCountryApi
import com.airalo.example.offer.api.model.CountryDTO
import com.airalo.example.offer.api.model.OfferDTO
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
    private val dummyCountryApi = CountriesApi(baseUrl = ApiClient.BASE_URL, httpClient = createMockClient(Content("")))
    private val dummyOffersApi = OffersInCountryApi(baseUrl = ApiClient.BASE_URL, httpClient = createMockClient(Content("")))

    private val serializedCountries = Resource("src/commonTest/resources/Countries.json").readText()
    private val countries = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }.decodeFromString(
        ListSerializer(CountryDTO.serializer()),
        serializedCountries,
    )
    private val serializedOffers = Resource("src/commonTest/resources/singapore.json").readText()
    private val offers = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }.decodeFromString(
        OfferDTO.serializer(),
        serializedOffers,
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

            val repository = OfferRepository(apiEndpoint, dummyOffersApi)

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

            val repository = OfferRepository(apiEndpoint, dummyOffersApi)

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
    fun `When fetchOffersForCountry is called Then it returns an empty list if the API end point fails`() =
        runTest {
            // Arrange
            val expectedError = Error(serializedCountries)
            val apiEndpoint = OffersInCountryApi(
                baseUrl = ApiClient.BASE_URL,
                httpClient = createMockClient(
                    Content(""),
                    expectedError,
                    HttpStatusCode.NotAcceptable,
                ),
            )

            val repository = OfferRepository(dummyCountryApi, apiEndpoint)

            // Act
            val offers = repository.fetchOffersForCountry(Id(12))

            // Assert
            offers mustBe emptyList()
        }

    @Test
    fun `When fetchOffersForCountry is called Then it returns the list of offers provided by the API point`() =
        runTest {
            // Arrange
            val countryId = Id(23)
            val apiEndpoint = OffersInCountryApi(
                baseUrl = ApiClient.BASE_URL,
                httpClient = createMockClient(
                    Content(serializedOffers, mapOf("Content-Type" to listOf("application/json"))),
                    null,
                    HttpStatusCode.OK,
                ) {
                    url.encodedPath.split("/").last() mustBe countryId.id.toString()
                }.config {
                    install(ContentNegotiation) {
                        json(ApiClient.JSON_DEFAULT)
                    }
                },
            )

            val repository = OfferRepository(dummyCountryApi, apiEndpoint)

            // Act
            val offers = repository.fetchOffersForCountry(countryId)

            // Assert
            offers.size mustBe this@OfferRepositorySpec.offers.packages.size
            offers.first().operator.name mustBe this@OfferRepositorySpec.offers.packages.first().operator.title
            offers.first().operator.logo.url mustBe this@OfferRepositorySpec.offers.packages.first().operator.image.url
            offers.first().price.price mustBe this@OfferRepositorySpec.offers.packages.first().price
            offers.first().validity.validity mustBe this@OfferRepositorySpec.offers.packages.first().validity
            offers.first().volume.volume mustBe this@OfferRepositorySpec.offers.packages.first().data
            offers.first().location mustBe this@OfferRepositorySpec.offers.title

            offers.last().operator.name mustBe this@OfferRepositorySpec.offers.packages.last().operator.title
            offers.last().operator.logo.url mustBe this@OfferRepositorySpec.offers.packages.last().operator.image.url
            offers.last().price.price mustBe this@OfferRepositorySpec.offers.packages.last().price
            offers.last().validity.validity mustBe this@OfferRepositorySpec.offers.packages.last().validity
            offers.last().volume.volume mustBe this@OfferRepositorySpec.offers.packages.last().data
            offers.last().location mustBe this@OfferRepositorySpec.offers.title
        }

    @Test
    fun `It fulfils OfferRepositoryContract`() {
        OfferRepository(dummyCountryApi, dummyOffersApi) fulfils OfferRepositoryContract::class
    }
}
