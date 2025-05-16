package com.airalo.example.offer.presentation.ui.molecule

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.airalo.example.offer.R
import com.airalo.example.offer.domain.entity.Price
import com.airalo.example.offer.presentation.ui.atom.OfferDetailActionSlot
import com.airalo.example.offer.presentation.ui.atom.PurchaseButtonLabel
import com.airalo.example.offer.presentation.ui.token.Color
import com.airalo.example.offer.presentation.ui.token.Shape
import com.airalo.example.offer.presentation.ui.token.Size

@Composable
fun PurchaseButton(price: Price) {
    OfferDetailActionSlot {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .height(Size.surface.purchaseButton.height)
                    .width(Size.surface.purchaseButton.width)
                    .border(
                        width = Size.border.xs,
                        color = Color.detailDivider,
                        shape = Shape.medium,
                    ),
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(Size.surface.purchaseButtonLabel.height),
                ) {
                    PurchaseButtonLabel(
                        content = stringResource(R.string.purchase_button, price.price),
                        contentDescription = stringResource(R.string.purchase_button_accessibility, price.price),
                    )
                }
            }
        }
    }
}
