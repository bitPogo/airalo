package com.airalo.example.offer.domain.interactor

import app.cash.turbine.test
import com.airalo.example.offer.api.model.OfferDTO
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.offer.domain.entity.Offer
import com.airalo.example.offer.domain.entity.Operator
import com.airalo.example.offer.domain.entity.OperatorLogoUrl
import com.airalo.example.offer.domain.entity.Price
import com.airalo.example.offer.domain.entity.Validity
import com.airalo.example.offer.domain.entity.Volume
import com.airalo.example.offer.domain.repository.OfferRepositoryContract
import com.airalo.example.offer.presentation.interactor.OfferPackageInteractorContract
import com.goncalossilva.resources.Resource
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

class OfferPackageInteractorSpec {
    private val offers = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }.decodeFromString(
        OfferDTO.serializer(),
        Resource("src/commonTest/resources/singapore.json").readText(),
    ).packages.map { dto ->
        Offer(
            operator = Operator(
                name = dto.operator.title,
                logo = OperatorLogoUrl(dto.operator.image.url),
            ),
            price = Price(dto.price),
            validity = Validity(dto.validity),
            volume = Volume(dto.data),
            location = "Singapore",
        )
    }

    @Test
    fun `When loadOffersForCountry is called Then it propagates the offers provided by the OfferRepository`() =
        runTest {
            val id = Id(12)
            // Arrange
            val repository = FakeOfferRepository(offers)
            val offerInteractor = OfferInteractor(repository)

            offerInteractor.offers.test {
                // Act
                offerInteractor.loadOffersForCountry(id)

                // Assert
                awaitItem() mustBe offers
                id mustBe repository.id
            }
        }

    @Test
    fun `It fulfils the OfferPackageInteractorContract`() {
        OfferInteractor(FakeOfferRepository(emptyList())) fulfils OfferPackageInteractorContract::class
    }

    private class FakeOfferRepository(
        private val offers: List<Offer>,
    ) : OfferRepositoryContract {
        private var _id: Id? = null
        val id: Id
            get() = _id!!

        override suspend fun listPopularCountries(): List<Country> = TODO("Not yet implemented")

        override suspend fun fetchOffersForCountry(id: Id): List<Offer> = offers.also { _id = id }
    }
}
