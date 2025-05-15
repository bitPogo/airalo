package com.airalo.example.offer.presentation.interactor

import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.offer.domain.entity.Offer
import kotlinx.coroutines.flow.SharedFlow

/**
 * Contract for the OfferPackageInteractor, which deal with the Offer retrieval.
 */
interface OfferPackageInteractorContract {
    /**
     * Observable ([SharedFlow]) which emits [List]s of [Offer]s.
     */
    val offers: SharedFlow<List<Offer>>

    /**
     * Propagates a list of offers of a given country represented by its [Id] through [offers].
     */
    suspend fun loadOffersForCountry(countryId: Id)
}
