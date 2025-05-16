@file:Suppress("ktlint:standard:max-line-length", "ktlint:standard:backing-property-naming")

package com.airalo.example.offer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
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
import com.airalo.example.offer.presentation.interactor.CountryOfferOverviewInteractorContract
import com.airalo.example.offer.presentation.interactor.OfferPackageInteractorContract
import com.airalo.example.offer.presentation.model.OfferPackageStateHolder
import com.airalo.example.offer.presentation.model.OfferPackageUiState
import com.airalo.example.offer.presentation.ui.command.LoadOfferPackageForCountryCommand
import com.goncalossilva.resources.Resource
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@ExperimentalCoroutinesApi
class OfferPackageViewModelSpec {
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
            location = dto.title,
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `When the LoadOfferPackageForCountryCommand is executed Then it propagates Success if there are offers`() =
        runTest {
            // Arrange
            val countryId = Id(23)
            val interactor = InteractorFake(mutableListOf(offers))
            val viewModel = OfferViewModel(DummyCountryInteractor, interactor)
            val router: NavController = mockk {
                every { navigate(any<String>()) } returns Unit
            }
            viewModel.offers.test {
                // Act
                viewModel(LoadOfferPackageForCountryCommand(countryId, router))
                advanceUntilIdle()

                // Assert
                awaitItem() mustBe OfferPackageUiState.Initial
                awaitItem() mustBe OfferPackageUiState.Loading(emptyList())
                awaitItem() mustBe OfferPackageUiState.Success(offers)
                interactor.id mustBe countryId
            }
        }

    @Test
    fun `When the LoadOfferPackageForCountryCommand is executed Then it propagates an Error if there are no offers`() =
        runTest {
            // Arrange
            val countryId = Id(23)
            val interactor = InteractorFake(mutableListOf(emptyList()))
            val viewModel = OfferViewModel(
                DummyCountryInteractor,
                interactor,
            )
            val router: NavController = mockk {
                every { navigate(any<String>()) } returns Unit
            }
            viewModel.offers.test {
                // Act
                viewModel(LoadOfferPackageForCountryCommand(countryId, router))
                advanceUntilIdle()

                // Assert
                awaitItem() mustBe OfferPackageUiState.Initial
                awaitItem() mustBe OfferPackageUiState.Loading(emptyList())
                awaitItem() mustBe OfferPackageUiState.Error(emptyList())
                interactor.id mustBe countryId
            }
        }

    @Test
    fun `Given offers had been successful resolved When the LoadOfferPackageForCountryCommand is executed Then it propagates an Error while keeping the previous result if no offers had been propagated`() =
        runTest {
            // Arrange
            val countryId = Id(23)
            val interactor = InteractorFake(mutableListOf(offers, emptyList()))
            val viewModel = OfferViewModel(DummyCountryInteractor, interactor)
            val router: NavController = mockk {
                every { navigate(any<String>()) } returns Unit
            }

            viewModel.offers.test {
                // Act
                viewModel(LoadOfferPackageForCountryCommand(countryId, router))
                advanceUntilIdle()

                // Assert
                awaitItem() mustBe OfferPackageUiState.Initial
                awaitItem() mustBe OfferPackageUiState.Loading(emptyList())
                awaitItem() mustBe OfferPackageUiState.Success(offers)

                // Act
                viewModel(LoadOfferPackageForCountryCommand(countryId, router))
                advanceUntilIdle()

                // Assert
                awaitItem() mustBe OfferPackageUiState.Loading(offers)
                awaitItem() mustBe OfferPackageUiState.Error(offers)
                interactor.id mustBe countryId
            }
        }

    @Test
    fun `It fulfils the OfferPackageStateHolder`() {
        OfferViewModel(DummyCountryInteractor, InteractorFake()) fulfils OfferPackageStateHolder::class
    }

    @Test
    fun `It fulfils ViewModel`() {
        OfferViewModel(DummyCountryInteractor, InteractorFake()) fulfils ViewModel::class
    }

    private object DummyCountryInteractor : CountryOfferOverviewInteractorContract {
        override val countries: SharedFlow<List<Country>> = MutableSharedFlow<List<Country>>().asSharedFlow()

        override suspend fun listPopularCountries() = TODO("Not yet implemented")
    }

    private class InteractorFake(
        private val _offers: MutableList<List<Offer>> = mutableListOf(),
    ) : OfferPackageInteractorContract {
        private var _id: Id? = null
        val id: Id
            get() = _id!!
        private val _toPropagate: MutableSharedFlow<List<Offer>> = MutableSharedFlow()
        override val offers: SharedFlow<List<Offer>> = _toPropagate.asSharedFlow()

        override suspend fun loadOffersForCountry(countryId: Id) {
            _id = countryId
            _toPropagate.emit(_offers.removeAt(0))
        }
    }
}
