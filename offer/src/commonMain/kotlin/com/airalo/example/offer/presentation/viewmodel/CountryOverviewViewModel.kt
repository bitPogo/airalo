package com.airalo.example.offer.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airalo.example.command.Command
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.presentation.interactor.CountryOfferOverviewInteractorContract
import com.airalo.example.offer.presentation.model.CountryOfferOverviewStateHolder
import com.airalo.example.offer.presentation.model.CountryOverviewUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class CountryOverviewViewModel(
    private val interactor: CountryOfferOverviewInteractorContract,
) :
    CountryOfferOverviewStateHolder,
        OfferCommandExecutor<OfferCommandReceiver>,
        LoadPopularOfferCountriesCommandReceiverContract,
        ViewModel() {
    private val _countries: MutableStateFlow<CountryOverviewUiState> = MutableStateFlow(CountryOverviewUiState.Initial)
    override val countries: StateFlow<CountryOverviewUiState> = _countries.asStateFlow()

    init {
        interactor.countries.onEach { countries ->
            if (countries.isEmpty()) {
                propagateError()
            } else {
                propagateSuccess(countries)
            }
        }.launchIn(viewModelScope)
    }

    private fun propagateSuccess(countries: List<Country>) {
        _countries.update {
            CountryOverviewUiState.Success(countries)
        }
    }

    private fun propagateError() {
        _countries.update {
            CountryOverviewUiState.Error(countries.value.countries)
        }
    }

    override fun loadPopularOfferCountries() {
        _countries.update { CountryOverviewUiState.Loading(countries.value.countries) }
        viewModelScope.launch {
            interactor.listPopularCountries()
        }
    }

    override fun invoke(command: Command<in OfferCommandReceiver>) {
        command.invoke(this)
    }
}
