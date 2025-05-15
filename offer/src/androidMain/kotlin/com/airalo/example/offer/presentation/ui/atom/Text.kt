package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import com.airalo.example.offer.presentation.ui.token.Font
import com.airalo.example.offer.presentation.ui.token.Font.overviewCountryName

@Composable
fun CountryName(content: String) {
    Text(
        content,
        textAlign = TextAlign.Start,
        style = overviewCountryName,
    )
}

@Composable
fun SectionTitle(
    content: String,
    contentDescription: String,
) {
    Text(
        content,
        textAlign = TextAlign.Start,
        style = Font.sectionTitle,
        modifier = Modifier
            .semantics {
                this.contentDescription = contentDescription
            },
    )
}

@Composable
fun ScreenTitle(content: String) {
    Text(
        content,
        textAlign = TextAlign.Start,
        style = Font.screenTitle,
    )
}

@Composable
fun OfferDetailLabel(
    content: String,
    contentDescription: String,
) {
    Text(
        content,
        textAlign = TextAlign.Start,
        style = Font.offerDetailLabel,
        modifier = Modifier
            .semantics {
                this.contentDescription = contentDescription
            },
    )
}

@Composable
fun OfferDetailValue(
    content: String,
    contentDescription: String,
) {
    Text(
        content,
        textAlign = TextAlign.Unspecified,
        style = Font.offerDetailValue,
        modifier = Modifier
            .semantics {
                this.contentDescription = contentDescription
            },
    )
}

@Composable
fun PurchaseButtonLabel(
    content: String,
    contentDescription: String,
) {
    Text(
        content,
        textAlign = TextAlign.Start,
        style = Font.offerButtonLabel,
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                this.contentDescription = contentDescription
            },
    )
}
