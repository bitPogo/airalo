package com.airalo.example.offer.di

import com.airalo.example.offer.api.client.CountriesApi
import com.airalo.example.offer.api.client.OffersInCountryApi
import com.airalo.example.offer.data.client.di.resolveClientKoin
import com.airalo.example.offer.data.repository.OfferRepository
import com.airalo.example.offer.domain.interactor.OfferInteractor
import com.airalo.example.offer.domain.repository.OfferRepositoryContract
import com.airalo.example.offer.presentation.interactor.CountryOfferOverviewInteractorContract
import com.airalo.example.offer.presentation.interactor.OfferPackageInteractorContract
import com.airalo.example.offer.presentation.viewmodel.OfferViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.binds
import org.koin.dsl.module


fun resolveOfferKoin() = module {
    includes(resolveClientKoin())

    single {
        OffersInCountryApi(
            baseUrl = get(qualifier = named("BaseUrl")),
            httpClient = get()
        )
    }
    single {
        CountriesApi(
            baseUrl = get(qualifier = named("BaseUrl")),
            httpClient = get()
        )
    }

    factory<OfferRepositoryContract> { OfferRepository(get(), get()) }
    factory { OfferInteractor(get()) } binds arrayOf(
        CountryOfferOverviewInteractorContract::class,
        OfferPackageInteractorContract::class
    )

    viewModel { OfferViewModel(get(), get()) }
}
