package com.airalo.example.offer.presentation.model

import com.airalo.example.offer.domain.entity.Country
import kotlinx.coroutines.flow.StateFlow

/**
 * States for the Country Offer Overview of the UI.
 *
 * @property countries The list of countries to be used by the UI.
 */
sealed class CountryOverviewUiState(open val countries: List<Country>) {
    /**
     * The initial state before any data is loaded.
     */
    data object Initial : CountryOverviewUiState(emptyList())

    /**
     * Represents the state when the data is being loaded.
     *
     * @property countries The list of countries carried over from the previous state.
     */
    data class Loading(override val countries: List<Country>) : CountryOverviewUiState(countries)

    /**
     * Represents the state when the data has been successfully loaded.
     *
     * @property countries The list of countries freshly resolved from the interactor.
     */
    data class Success(override val countries: List<Country>) : CountryOverviewUiState(countries)

    /**
     * Represents an state of error which happened while loading the data.
     *
     * @property countries The list of countries carried over from the previous state.
     */
    data class Error(override val countries: List<Country>) : CountryOverviewUiState(countries)
}

/**
 * Contract for to hold states of the country overview.
 */
interface CountryOfferOverviewStateHolder {
    /**
     * The current state of the country overview.
     *
     * The current [CountryOverviewUiState] allows other components to collect the state and react to changes.
     */
    val countries: StateFlow<CountryOverviewUiState>
}
