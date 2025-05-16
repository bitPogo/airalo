package com.airalo.example.offer.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.airalo.example.offer.presentation.ui.screen.OfferPackageScreen
import com.airalo.example.offer.presentation.ui.screen.CountryOfferOverview
import com.airalo.example.offer.presentation.viewmodel.OfferViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.Koin

enum class OfferRoutes(val route: String) {
    COUNTRY_OVERVIEW("country_overview"),
    OFFER_PACKAGE("offer_package"),
}

// This just for now...since it should be used together with navigation
fun NavGraphBuilder.OfferRouter(
    navController: NavHostController,
    koin: Koin,
) {
    // This is not great since it avoid using the ViewModel Factory
    val viewModel = koin.get<OfferViewModel>()

    composable(OfferRoutes.COUNTRY_OVERVIEW.route) {
        CountryOfferOverview(viewModel, viewModel)
    }

    composable(OfferRoutes.OFFER_PACKAGE.route) {
        OfferPackageScreen(
            stateHolder = viewModel,
            goBack = { navController.popBackStack() }
        )
    }
}
