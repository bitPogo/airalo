package com.airalo.example.offer.presentation.viewmodel

import com.airalo.example.command.Command
import com.airalo.example.command.CommandExecutor
import com.airalo.example.command.CommandReceiver
import com.airalo.example.offer.domain.entity.Id

/**
 * Marker interface for an offer command receivers.
 */
sealed interface OfferCommandReceiver : CommandReceiver

fun interface OfferCommandExecutor<T : OfferCommandReceiver> : CommandExecutor<T> {
    override operator fun invoke(command: Command<in T>)
}

/**
 * Triggers the loading of popular countries where offers exist.
 */
fun interface LoadPopularOfferCountriesCommandReceiverContract : OfferCommandReceiver {
    /**
     * Loads popular countries where offers exist.
     */
    fun loadPopularOfferCountries()
}

/**
 * Triggers the loading of an offer package for a country.
 */
fun interface LoadOffersForCountryCommandReceiverContract : OfferCommandReceiver {
    /**
     * Loads offers for the country with the given ID.
     *
     * @param countryId The unique identifier of the country for which to load offers.
     */
    fun loadOffersForCountry(countryId: Id)
}
