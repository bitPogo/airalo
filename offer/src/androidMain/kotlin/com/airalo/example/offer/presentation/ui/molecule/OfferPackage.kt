package com.airalo.example.offer.presentation.ui.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.zIndex
import com.airalo.example.offer.R
import com.airalo.example.offer.domain.entity.Offer
import com.airalo.example.offer.domain.entity.Operator
import com.airalo.example.offer.presentation.ui.atom.OfferDetailActionSlot
import com.airalo.example.offer.presentation.ui.atom.OfferSubTitle
import com.airalo.example.offer.presentation.ui.atom.OperatorLogo
import com.airalo.example.offer.presentation.ui.atom.SectionTitle
import com.airalo.example.offer.presentation.ui.atom.ValueDivider
import com.airalo.example.offer.presentation.ui.shadow
import com.airalo.example.offer.presentation.ui.token.Color
import com.airalo.example.offer.presentation.ui.token.Shape
import com.airalo.example.offer.presentation.ui.token.Size
import com.airalo.example.offer.presentation.ui.token.Spacing

@Composable
private fun OperatorLabel(
    operator: Operator,
    location: String,
) {
    val accessibilityLabel = stringResource(
        R.string.offer_section_title_accessibility,
        operator.name,
    )

    OfferDetailActionSlot {
        Column(
            modifier = Modifier
                .padding(Spacing.m)
                .semantics {
                    this.contentDescription = accessibilityLabel
                },
        ) {
            SectionTitle(
                operator.name,
                null,
            )
            OfferSubTitle(location)
        }
    }
}

@Composable
fun OfferPackage(offer: Offer) {
    Box(
        modifier = Modifier
            .width(Size.surface.offerPackage.width)
            .height(Size.surface.offerPackage.height)
            .shadow(),
    ) {
        Box(
            modifier = Modifier
                .height(Size.surface.offerPackageCard.height)
                .fillMaxWidth()
                .offset(y = Spacing.m)
                .zIndex(1f)
                .background(
                    shape = Shape.medium,
                    brush = Brush.horizontalGradient(
                        listOf(Color.vividOrange, Color.lightYellow),
                    ),
                ),
        ) {
            Column {
                OperatorLabel(
                    operator = offer.operator,
                    location = offer.location,
                )
                ValueDivider()
                DataVolume(offer.volume)
                ValueDivider()
                ValidityDuration(offer.validity)
                ValueDivider()
                PurchaseButton(offer.price)
            }
        }

        Box(
            contentAlignment = Alignment.TopEnd,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = Spacing.m)
                .zIndex(2f),
        ) {
            OperatorLogo(offer.operator)
        }
    }
}
