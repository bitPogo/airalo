package com.airalo.example.offer.presentation.ui.molecule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.airalo.example.offer.R
import com.airalo.example.offer.presentation.ui.atom.SectionTitle
import com.airalo.example.offer.presentation.ui.token.Size
import com.airalo.example.offer.presentation.ui.token.Spacing

@Composable
fun CountryListHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(
                Size.surface.countryListHeader.height,
            )
            .padding(bottom = Spacing.m),
    ) {
        SectionTitle(
            content = stringResource(R.string.country_list_header),
            contentDescription = stringResource(R.string.country_list_header_accessibility),
        )
    }
}
