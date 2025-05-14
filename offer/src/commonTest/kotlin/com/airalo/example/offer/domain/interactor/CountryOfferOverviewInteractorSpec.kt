package com.airalo.example.offer.domain.interactor

import app.cash.turbine.test
import com.airalo.example.offer.api.model.CountryDTO
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.entity.Flag
import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.offer.domain.repository.OfferRepositoryContract
import com.airalo.example.offer.presentation.interactor.CountryOfferOverviewInteractorContract
import com.goncalossilva.resources.Resource
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

class CountryOfferOverviewInteractorSpec {
    private val serializedCountries = Resource("src/commonTest/resources/Countries.json").readText()
    private val countries = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }.decodeFromString(
        ListSerializer(CountryDTO.serializer()),
        serializedCountries,
    ).map {
        Country(
            id = Id(it.id),
            flag = Flag(it.image.url),
            name = it.title,
        )
    }

    @Test
    fun `When listPopularCountries is called Then it propagates the countries provided by the OfferRepository`() =
        runTest {
            // Arrange
            val repository = FakeOfferRepository(countries)
            val offerInteractor = OfferInteractor(repository)

            offerInteractor.countries.test {
                // Act
                offerInteractor.listPopularCountries()

                // Assert
                awaitItem() mustBe countries
            }
        }

    @Test
    fun `It fulfils the CountryOfferOverviewInteractorContract`() {
        OfferInteractor(FakeOfferRepository(emptyList())) fulfils CountryOfferOverviewInteractorContract::class
    }

    private class FakeOfferRepository(
        private val countries: List<Country>,
    ) : OfferRepositoryContract {
        override suspend fun listPopularCountries(): List<Country> = countries
    }
}
