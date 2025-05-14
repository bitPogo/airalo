package com.airalo.example.offer.presentation

import com.airalo.example.offer.domain.entity.Country
import kotlinx.coroutines.flow.SharedFlow

/**
 * This defines the a OfferOverviewInteractor.
 * It deals business logic related to the country overview for offers.
 */
interface CountryOfferOverviewInteractorContract {
    /**
     * Observable ([SharedFlow]) which emits [List]s of [Country]s.
     */
    val countries: SharedFlow<List<Country>>

    /**
     * Propagates the list of popular countries through [countries].
     */
    suspend fun listPopularCountries()
}
