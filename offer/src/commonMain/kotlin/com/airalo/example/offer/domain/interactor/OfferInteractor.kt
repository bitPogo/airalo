package com.airalo.example.offer.domain.interactor

import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.repository.OfferRepositoryContract
import com.airalo.example.offer.presentation.CountryOfferOverviewInteractorContract
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class OfferInteractor(
    private val repository: OfferRepositoryContract,
) : CountryOfferOverviewInteractorContract {
    private val _countries: MutableSharedFlow<List<Country>> = MutableSharedFlow()
    override val countries: SharedFlow<List<Country>> = _countries.asSharedFlow()

    override suspend fun listPopularCountries() {
        _countries.emit(repository.listPopularCountries())
    }
}
