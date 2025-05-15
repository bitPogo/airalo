package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.invisibleToUser
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.airalo.example.offer.presentation.ui.token.Color
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
    contentDescription: String?,
) {
    Text(
        content,
        textAlign = TextAlign.Start,
        style = Font.sectionTitle,
        modifier = Modifier
            .semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                } else {
                    this.invisibleToUser()
                }
            },
    )
}

@Composable
fun ScreenTitle(
    content: String,
    contentDescription: String? = null,
) {
    Text(
        content,
        textAlign = TextAlign.Start,
        style = Font.screenTitle,
        modifier = Modifier
            .semantics {
            contentDescription?.let {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                } else {
                    this.invisibleToUser()
                }
            }
        },
    )
}

@Composable
fun OfferSubTitle(content: String) {
    Text(
        content,
        textAlign = TextAlign.Start,
        style = Font.offerSubtitle,
        modifier = Modifier
            .semantics {
                this.invisibleToUser()
            },
    )
}

@Composable
fun OfferDetailLabel(
    content: String,
    contentDescription: String?,
) {
    Text(
        content,
        textAlign = TextAlign.Start,
        style = Font.offerDetailLabel,
        modifier = Modifier
            .semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                } else {
                    this.invisibleToUser()
                }
            },
    )
}

@Composable
fun OfferDetailValue(
    content: String,
    contentDescription: String?,
) {
    Text(
        content,
        textAlign = TextAlign.Unspecified,
        style = Font.offerDetailValue,
        modifier = Modifier
            .semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                } else {
                    this.invisibleToUser()
                }
            },
    )
}

@Composable
fun PurchaseButtonLabel(
    content: String,
    contentDescription: String?,
) {
    Text(
        content,
        textAlign = TextAlign.Start,
        style = Font.offerButtonLabel,
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                if (contentDescription != null) {
                    this.contentDescription = contentDescription
                } else {
                    this.invisibleToUser()
                }
            },
    )
}
