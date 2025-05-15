package com.airalo.example.offer.domain.interactor

import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.offer.domain.entity.Offer
import com.airalo.example.offer.domain.repository.OfferRepositoryContract
import com.airalo.example.offer.presentation.interactor.CountryOfferOverviewInteractorContract
import com.airalo.example.offer.presentation.interactor.OfferPackageInteractorContract
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

internal class OfferInteractor(
    private val repository: OfferRepositoryContract,
) : CountryOfferOverviewInteractorContract, OfferPackageInteractorContract {
    private val _countries: MutableSharedFlow<List<Country>> = MutableSharedFlow()
    override val countries: SharedFlow<List<Country>> = _countries.asSharedFlow()

    override suspend fun listPopularCountries() {
        _countries.emit(repository.listPopularCountries())
    }

    private val _offers: MutableSharedFlow<List<Offer>> = MutableSharedFlow()
    override val offers: SharedFlow<List<Offer>> = _offers.asSharedFlow()

    override suspend fun loadOffersForCountry(countryId: Id) {
        _offers.emit(repository.fetchOffersForCountry(countryId))
    }
}
