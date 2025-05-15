@file:Suppress("ktlint:standard:max-line-length", "ktlint:standard:backing-property-naming")

package com.airalo.example.offer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import app.cash.turbine.test
import com.airalo.example.offer.api.model.CountryDTO
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.entity.CountryFlagUri
import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.offer.domain.entity.Offer
import com.airalo.example.offer.presentation.interactor.CountryOfferOverviewInteractorContract
import com.airalo.example.offer.presentation.interactor.OfferPackageInteractorContract
import com.airalo.example.offer.presentation.model.CountryOfferOverviewStateHolder
import com.airalo.example.offer.presentation.model.CountryOverviewUiState
import com.airalo.example.offer.presentation.ui.command.LoadPopularOfferCountriesCommand
import com.goncalossilva.resources.Resource
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
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import tech.antibytes.util.test.fulfils
import tech.antibytes.util.test.mustBe

@ExperimentalCoroutinesApi
class CountryOverviewViewModelSpec {
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
            flag = CountryFlagUri(it.image.url),
            name = it.title,
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
    fun `When the LoadPopularCountriesCommand is executed Then it propagates Success if there are countries`() =
        runTest {
            // Arrange
            val viewModel = OfferViewModel(InteractorFake(mutableListOf(countries)), DummyOfferInteractor)
            viewModel.countries.test {
                // Act
                viewModel(LoadPopularOfferCountriesCommand)
                advanceUntilIdle()

                // Assert
                awaitItem() mustBe CountryOverviewUiState.Initial
                awaitItem() mustBe CountryOverviewUiState.Loading(emptyList())
                awaitItem() mustBe CountryOverviewUiState.Success(countries)
            }
        }

    @Test
    fun `When the LoadPopularCountriesCommand is executed Then it propagates an Error if there are no countries`() =
        runTest {
            // Arrange
            val viewModel = OfferViewModel(InteractorFake(mutableListOf(emptyList())), DummyOfferInteractor)
            viewModel.countries.test {
                // Act
                viewModel(LoadPopularOfferCountriesCommand)
                advanceUntilIdle()

                // Assert
                awaitItem() mustBe CountryOverviewUiState.Initial
                awaitItem() mustBe CountryOverviewUiState.Loading(emptyList())
                awaitItem() mustBe CountryOverviewUiState.Error(emptyList())
            }
        }

    @Test
    fun `Given Countries had been successful resolved When the LoadPopularCountriesCommand is executed Then it propagates an Error while keeping the previous result if no countries had been propagated`() =
        runTest {
            // Arrange
            val viewModel = OfferViewModel(
                InteractorFake(
                    mutableListOf(
                        countries,
                        emptyList(),
                    ),
                ),
                DummyOfferInteractor,
            )
            viewModel.countries.test {
                // Act
                viewModel(LoadPopularOfferCountriesCommand)
                advanceUntilIdle()

                // Assert
                awaitItem() mustBe CountryOverviewUiState.Initial
                awaitItem() mustBe CountryOverviewUiState.Loading(emptyList())
                awaitItem() mustBe CountryOverviewUiState.Success(countries)

                // Act
                viewModel(LoadPopularOfferCountriesCommand)
                advanceUntilIdle()

                // Assert
                awaitItem() mustBe CountryOverviewUiState.Loading(countries)
                awaitItem() mustBe CountryOverviewUiState.Error(countries)
            }
        }

    @Test
    fun `It fulfils the CountryOfferOverviewStateHolder`() {
        OfferViewModel(InteractorFake(), DummyOfferInteractor) fulfils CountryOfferOverviewStateHolder::class
    }

    @Test
    fun `It fulfils ViewModel`() {
        OfferViewModel(InteractorFake(), DummyOfferInteractor) fulfils ViewModel::class
    }

    private class InteractorFake(
        private val _countries: MutableList<List<Country>> = mutableListOf(),
    ) : CountryOfferOverviewInteractorContract {
        private val _toPropagate: MutableSharedFlow<List<Country>> = MutableSharedFlow()
        override val countries: SharedFlow<List<Country>> = _toPropagate.asSharedFlow()

        override suspend fun listPopularCountries() {
            _toPropagate.emit(_countries.removeAt(0))
        }
    }

    private object DummyOfferInteractor : OfferPackageInteractorContract {
        override val offers: SharedFlow<List<Offer>> = MutableSharedFlow<List<Offer>>().asSharedFlow()

        override suspend fun loadOffersForCountry(countryId: Id) = TODO("Not yet implemented")
    }
}
