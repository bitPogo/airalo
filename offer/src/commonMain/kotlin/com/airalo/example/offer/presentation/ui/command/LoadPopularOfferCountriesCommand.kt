package com.airalo.example.offer.presentation.ui.command

import com.airalo.example.command.Command
import com.airalo.example.offer.presentation.viewmodel.LoadPopularOfferCountriesCommandReceiverContract
import com.airalo.example.offer.presentation.viewmodel.OfferCommandReceiver

/**
 * Command to popular countries where offers exist.
 */
data object LoadPopularOfferCountriesCommand : Command<OfferCommandReceiver> {
    /**
     * Executes the command to  popular countries where offers exist.
     *
     * @param receiver The [OfferCommandReceiver] on which to execute this command.
     */
    override fun invoke(receiver: OfferCommandReceiver) {
        (receiver as LoadPopularOfferCountriesCommandReceiverContract).loadPopularOfferCountries()
    }
}
