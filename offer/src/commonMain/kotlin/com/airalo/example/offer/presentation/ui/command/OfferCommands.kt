package com.airalo.example.offer.presentation.ui.command

import androidx.navigation.NavController
import com.airalo.example.command.Command
import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.offer.presentation.navigation.OfferRoutes
import com.airalo.example.offer.presentation.viewmodel.LoadOffersForCountryCommandReceiverContract
import com.airalo.example.offer.presentation.viewmodel.LoadPopularOfferCountriesCommandReceiverContract
import com.airalo.example.offer.presentation.viewmodel.OfferCommandReceiver

sealed interface OfferCommand : Command<OfferCommandReceiver>

/**
 * Command to retrieve popular countries where offers exist.
 */
data object LoadPopularOfferCountriesCommand : OfferCommand {
    /**
     * Executes the command to  popular countries where offers exist.
     *
     * @param receiver The [OfferCommandReceiver] on which to execute this command.
     */
    override fun invoke(receiver: OfferCommandReceiver) {
        (receiver as LoadPopularOfferCountriesCommandReceiverContract).loadPopularOfferCountries()
    }
}

/**
 * Command to load offers for a given country.
 */
class LoadOfferPackageForCountryCommand(
    private val countryId: Id,
    private val router: NavController,
) : OfferCommand {
    /**
     * Executes the command to  popular countries where offers exist.
     *
     * @param receiver The [OfferCommandReceiver] on which to execute this command.
     */
    override fun invoke(receiver: OfferCommandReceiver) {
        router.navigate(OfferRoutes.OFFER_PACKAGE.route)
        (receiver as LoadOffersForCountryCommandReceiverContract).loadOffersForCountry(countryId)
    }
}
