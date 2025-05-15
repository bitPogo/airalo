package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.airalo.example.offer.R
import com.airalo.example.offer.presentation.ui.token.Size
import com.airalo.example.offer.presentation.ui.token.Spacing

@Composable
fun GoBackButton(action: Function0<Unit>) {
    val contentDescription = stringResource(R.string.go_back)

    Box(
        modifier = Modifier
            .width(Size.surface.gobackButton.width)
            .height(Size.surface.gobackButton.height)
            .clickable(
                enabled = true,
                onClick = action,
            ).semantics {
                this.contentDescription = contentDescription
            },
        contentAlignment = Alignment.CenterStart,
    ) {
        Image(
            painter = painterResource(R.drawable.ic_back),
            contentDescription = null,
            modifier = Modifier
                .padding(start = Spacing.xxxs)
                .width(Size.surface.innerGoBackButton.width)
                .height(Size.surface.innerGoBackButton.height),
        )
    }
}
