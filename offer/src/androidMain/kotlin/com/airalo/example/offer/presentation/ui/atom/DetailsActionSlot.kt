package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airalo.example.offer.presentation.ui.token.Size
import com.airalo.example.offer.presentation.ui.token.Spacing

@Composable
fun OfferDetailActionSlot(slot: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .padding(Spacing.m)
            .height(Size.surface.detailBoxActionField.height)
            .fillMaxWidth(),
    ) {
        slot()
    }
}
