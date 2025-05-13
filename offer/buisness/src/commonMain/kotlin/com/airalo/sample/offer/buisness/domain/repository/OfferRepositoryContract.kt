package com.airalo.sample.offer.buisness.domain.repository

import com.airalo.sample.offer.buisness.domain.entity.Country

/**
 * Contract for the OfferRepository, which deals with the main data retrieval
 */
fun interface OfferRepositoryContract {
    /**
     * Retrieves a list of countries by their popularity.
     *
     * @return A [List] of [Country] or an empty list on error sign film.
     */
    suspend fun listPopularCountries(): List<Country>
}
