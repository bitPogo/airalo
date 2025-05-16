package com.airalo.example.offer.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.airalo.example.offer.R
import com.airalo.example.offer.presentation.model.CountryOfferOverviewStateHolder
import com.airalo.example.offer.presentation.ui.command.LoadPopularOfferCountriesCommand
import com.airalo.example.offer.presentation.ui.molecule.ScreenHeader
import com.airalo.example.offer.presentation.ui.organism.CountryList
import com.airalo.example.offer.presentation.ui.token.Color
import com.airalo.example.offer.presentation.viewmodel.OfferCommandExecutor
import com.airalo.example.offer.presentation.viewmodel.OfferCommandReceiver

@Composable
fun CountryOfferOverview(
    stateHolder: CountryOfferOverviewStateHolder,
    router: NavController,
    executor: OfferCommandExecutor<out OfferCommandReceiver>,
) {
    val countries = stateHolder.countries.collectAsState()
    LaunchedEffect(true) {
        executor(LoadPopularOfferCountriesCommand)
    }

    Column(
        modifier = Modifier.background(color = Color.lightGray),
    ) {
        ScreenHeader(stringResource(R.string.country_offer_screen_header))
        CountryList(countries.value.countries, router, executor)
    }
}
