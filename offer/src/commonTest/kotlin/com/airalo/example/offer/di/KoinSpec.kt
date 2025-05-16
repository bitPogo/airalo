package com.airalo.example.offer.di

import com.airalo.example.offer.presentation.viewmodel.OfferViewModel
import kotlin.test.Test
import kotlin.test.assertNotNull
import org.koin.dsl.koinApplication

class KoinSpec {
    // Note there is an extension of Koin needed to just refer to the interfaces and still use the ViewModelFactory
    @Test
    fun `It resolves an OfferViewModel`() {
        val koin = koinApplication {
            modules(
                resolveOfferKoin(),
            )
        }

        // When
        val viewModel: OfferViewModel? = koin.koin.getOrNull()

        // Then
        assertNotNull(viewModel)
    }
}
