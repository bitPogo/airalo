package com.airalo.example.offer.presentation.ui.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.airalo.example.offer.domain.entity.Offer
import com.airalo.example.offer.domain.entity.Operator
import com.airalo.example.offer.domain.entity.OperatorLogoUrl
import com.airalo.example.offer.domain.entity.Price
import com.airalo.example.offer.domain.entity.Validity
import com.airalo.example.offer.domain.entity.Volume

@Preview(showBackground = true)
@Composable
fun OfferPackagePreview() {
    OfferPackage(
        Offer(
            operator = Operator(
                name = "Me",
                logo = OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg"),
            ),
            price = Price(9.99),
            validity = Validity("1 day"),
            volume = Volume("1 GB"),
            location = "Singapore",
        ),
    )
}
