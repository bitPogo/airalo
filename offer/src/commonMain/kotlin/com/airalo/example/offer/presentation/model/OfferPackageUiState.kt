package com.airalo.example.offer.presentation.model

import com.airalo.example.offer.domain.entity.Offer
import kotlinx.coroutines.flow.StateFlow

/**
 * States for the Offer Package of the UI.
 *
 * @property offers The list of offers to be used by the UI.
 */
sealed class OfferPackageUiState(open val offers: List<Offer>) {
    /**
     * The initial state before any data is loaded.
     */
    data object Initial : OfferPackageUiState(emptyList())

    /**
     * Represents the state when the data is being loaded.
     *
     * @property offers The list of offers carried over from the previous state.
     */
    data class Loading(override val offers: List<Offer>) : OfferPackageUiState(offers)

    /**
     * Represents the state when the data has been successfully loaded.
     *
     * @property offers The list of offers freshly resolved from the interactor.
     */
    data class Success(override val offers: List<Offer>) : OfferPackageUiState(offers)

    /**
     * Represents an state of error which happened while loading the data.
     *
     * @property offers The list of offers carried over from the previous state.
     */
    data class Error(override val offers: List<Offer>) : OfferPackageUiState(offers)
}

/**
 * Contract for to hold states of the offer packages.
 */
interface OfferPackageStateHolder {
    /**
     * The current state of the offer packages in a Offer.
     *
     * The current [OfferPackageUiState] allows other components to collect the state and react to changes.
     */
    val offers: StateFlow<OfferPackageUiState>
}
