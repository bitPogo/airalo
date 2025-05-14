package com.airalo.example.offer.presentation.viewmodel

import com.airalo.example.command.Command
import com.airalo.example.command.CommandExecutor
import com.airalo.example.command.CommandReceiver

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
