package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.airalo.example.offer.R
import com.airalo.example.offer.presentation.ui.token.Size.Surface.icon
import com.airalo.example.offer.presentation.ui.token.Size.Surface.trailingArrow

@Composable
private fun Icon(
    alignment: Alignment,
    slot: @Composable () -> Unit,
) {
    Box(
        modifier = Modifier
            .width(icon.width)
            .height(icon.height),
        contentAlignment = alignment,
    ) {
        slot()
    }
}

@Composable
fun TrailingArrow() {
    Icon(Alignment.CenterEnd) {
        Image(
            painter = painterResource(R.drawable.ic_arrow_right),
            contentDescription = null,
            modifier = Modifier.width(
                trailingArrow.width,
            ).height(
                trailingArrow.height,
            ),
        )
    }
}
