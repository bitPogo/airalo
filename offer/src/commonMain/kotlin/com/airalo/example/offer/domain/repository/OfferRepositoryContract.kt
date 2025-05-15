package com.airalo.example.offer.domain.repository

import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.offer.domain.entity.Offer

/**
 * Contract for the OfferRepository, which deals with the androidMain data retrieval
 */
interface OfferRepositoryContract {
    /**
     * Retrieves a list of countries by their popularity.
     *
     * @return A [List] of [Country] or an empty list on error sign film.
     */
    suspend fun listPopularCountries(): List<Country>

    /**
     * Fetches a list of offers available for a specific country.
     *
     * @param id a identifier ([Id]) of the country for which to fetch offers.
     * @return A [List] of [Offer]s available for the specified country,
     * or an empty list if no offers are found or an error occurs during fetching.
     */
    suspend fun fetchOffersForCountry(id: Id): List<Offer>
}
