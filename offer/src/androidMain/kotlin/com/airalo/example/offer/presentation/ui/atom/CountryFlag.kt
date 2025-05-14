package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import coil3.compose.AsyncImage
import com.airalo.example.offer.R
import com.airalo.example.offer.domain.entity.CountryFlagUri
import com.airalo.example.offer.presentation.ui.token.Size

@Composable
fun CountryFlag(uri: CountryFlagUri) {
    AsyncImage(
        model = uri.url,
        error = painterResource(R.drawable.flag_of_flags),
        contentDescription = null,
        modifier = Modifier
            .width(Size.surface.countryFlag.width)
            .height(Size.surface.countryFlag.height),
    )
}
