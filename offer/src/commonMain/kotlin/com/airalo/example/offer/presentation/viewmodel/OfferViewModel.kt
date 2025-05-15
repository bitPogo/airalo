package com.airalo.example.offer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airalo.example.command.Command
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.offer.domain.entity.Offer
import com.airalo.example.offer.presentation.interactor.CountryOfferOverviewInteractorContract
import com.airalo.example.offer.presentation.interactor.OfferPackageInteractorContract
import com.airalo.example.offer.presentation.model.CountryOfferOverviewStateHolder
import com.airalo.example.offer.presentation.model.CountryOverviewUiState
import com.airalo.example.offer.presentation.model.OfferPackageStateHolder
import com.airalo.example.offer.presentation.model.OfferPackageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class OfferViewModel(
    private val countryInteractor: CountryOfferOverviewInteractorContract,
    private val offerPackageInteractor: OfferPackageInteractorContract,
) :
    CountryOfferOverviewStateHolder,
        OfferPackageStateHolder,
        OfferCommandExecutor<OfferCommandReceiver>,
        LoadPopularOfferCountriesCommandReceiverContract,
        LoadOffersForCountryCommandReceiverContract,
        ViewModel() {
    private val _countries: MutableStateFlow<CountryOverviewUiState> = MutableStateFlow(CountryOverviewUiState.Initial)
    override val countries: StateFlow<CountryOverviewUiState> = _countries.asStateFlow()

    private val _offers: MutableStateFlow<OfferPackageUiState> = MutableStateFlow(OfferPackageUiState.Initial)
    override val offers: StateFlow<OfferPackageUiState> = _offers.asStateFlow()

    init {
        subscribeToCountryOverview()
        subscribeToOfferPackage()
    }

    private fun propagateCountryOverview(countries: List<Country>) {
        _countries.update {
            CountryOverviewUiState.Success(countries)
        }
    }

    private fun propagateCountryOverviewError() {
        _countries.update {
            CountryOverviewUiState.Error(countries.value.countries)
        }
    }

    private fun subscribeToCountryOverview() {
        countryInteractor.countries.onEach { countries ->
            if (countries.isEmpty()) {
                propagateCountryOverviewError()
            } else {
                propagateCountryOverview(countries)
            }
        }.launchIn(viewModelScope)
    }

    override fun loadPopularOfferCountries() {
        _countries.update { CountryOverviewUiState.Loading(countries.value.countries) }
        viewModelScope.launch {
            countryInteractor.listPopularCountries()
        }
    }

    private fun propagateOfferPackage(offers: List<Offer>) {
        _offers.update {
            OfferPackageUiState.Success(offers)
        }
    }

    private fun propagateOfferPackageError() {
        _offers.update {
            OfferPackageUiState.Error(offers.value.offers)
        }
    }

    private fun subscribeToOfferPackage() {
        offerPackageInteractor.offers.onEach {
            if (it.isEmpty()) {
                propagateOfferPackageError()
            } else {
                propagateOfferPackage(it)
            }
        }.launchIn(viewModelScope)
    }

    override fun loadOffersForCountry(countryId: Id) {
        _offers.update { OfferPackageUiState.Loading(offers.value.offers) }
        viewModelScope.launch {
            offerPackageInteractor.loadOffersForCountry(countryId)
        }
    }

    override fun invoke(command: Command<in OfferCommandReceiver>) {
        command.invoke(this)
    }
}
