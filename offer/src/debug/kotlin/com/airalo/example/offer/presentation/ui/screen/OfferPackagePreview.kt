package com.airalo.example.offer.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.airalo.example.offer.domain.entity.Offer
import com.airalo.example.offer.domain.entity.Operator
import com.airalo.example.offer.domain.entity.OperatorLogoUrl
import com.airalo.example.offer.domain.entity.Price
import com.airalo.example.offer.domain.entity.Validity
import com.airalo.example.offer.domain.entity.Volume
import com.airalo.example.offer.presentation.model.OfferPackageStateHolder
import com.airalo.example.offer.presentation.model.OfferPackageUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Preview
@Composable
fun OfferPackagePreviewWithoutContent() {
    OfferPackageScreen(
        OfferPackageStateHolderFake(MutableStateFlow(OfferPackageUiState.Initial)),
    ) {}
}

@Preview
@Composable
fun OfferPackagePreviewWithContent() {
    val offers = listOf(
        Offer(
            location = "United States",
            operator = Operator("Operator A", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
            price = Price(10.00),
            validity = Validity("7 Days"),
            volume = Volume("1 GB"),
        ),
        Offer(
            location = "Canada",
            operator = Operator("Operator B", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
            price = Price(15.00),
            validity = Validity("15 Days"),
            volume = Volume("3 GB"),
        ),
        Offer(
            location = "Mexico",
            operator = Operator("Operator C", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
            price = Price(20.00),
            validity = Validity("30 Days"),
            volume = Volume("5 GB"),
        ),
        Offer(
            location = "United States",
            operator = Operator("Operator A", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
            price = Price(5.00),
            validity = Validity("3 Days"),
            volume = Volume("500 MB"),
        ),
        Offer(
            location = "Canada",
            operator = Operator("Operator B", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
            price = Price(25.00),
            validity = Validity("30 Days"),
            volume = Volume("10 GB"),
        ),
        Offer(
            location = "Mexico",
            operator = Operator("Operator C", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
            price = Price(12.00),
            validity = Validity("7 Days"),
            volume = Volume("2 GB"),
        ),
        Offer(
            location = "United States",
            operator = Operator("Operator A", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
            price = Price(30.00),
            validity = Validity("60 Days"),
            volume = Volume("15 GB"),
        ),
        Offer(
            location = "Canada",
            operator = Operator("Operator B", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
            price = Price(8.00),
            validity = Validity("7 Days"),
            volume = Volume("1 GB"),
        ),
        Offer(
            location = "Mexico",
            operator = Operator("Operator C", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
            price = Price(18.00),
            validity = Validity("15 Days"),
            volume = Volume("4 GB"),
        ),
        Offer(
            location = "United States",
            operator = Operator("Operator A", OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg")),
            price = Price(40.00),
            validity = Validity("90 Days"),
            volume = Volume("20 GB"),
        ),
    )

    OfferPackageScreen(
        OfferPackageStateHolderFake(MutableStateFlow(OfferPackageUiState.Success(offers))),
    ) {}
}

private class OfferPackageStateHolderFake(
    override val offers: StateFlow<OfferPackageUiState>,
) : OfferPackageStateHolder
