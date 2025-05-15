package com.airalo.example.offer.presentation.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.airalo.example.offer.R
import com.airalo.example.offer.presentation.model.OfferPackageStateHolder
import com.airalo.example.offer.presentation.ui.molecule.ScreenHeader
import com.airalo.example.offer.presentation.ui.organism.OfferList
import com.airalo.example.offer.presentation.ui.token.Color

@Composable
fun OfferPackageScreen(
    stateHolder: OfferPackageStateHolder,
    goBack: Function0<Unit>,
) {
    val offers = stateHolder.offers.collectAsState()
    val location = offers.value.offers.firstOrNull()?.location ?: ""

    Column(
        modifier = Modifier.background(color = Color.lightGray),
    ) {
        ScreenHeader(
            location,
            stringResource(R.string.offer_screen_title_accessibility, location),
            goBack,
        )
        OfferList(offers.value.offers)
    }
}
