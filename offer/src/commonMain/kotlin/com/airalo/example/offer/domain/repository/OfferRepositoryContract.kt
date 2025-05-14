package com.airalo.example.offer.domain.repository

import com.airalo.example.offer.domain.entity.Country

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
