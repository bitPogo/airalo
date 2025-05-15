package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airalo.example.offer.presentation.ui.token.Size
import com.airalo.example.offer.presentation.ui.token.Spacing

@Composable
fun OfferDetailsValueSlot(
    left: @Composable BoxScope.() -> Unit,
    right: @Composable BoxScope.() -> Unit,
) {
    Row(
        modifier = Modifier
            .padding(Spacing.m)
            .height(Size.surface.detailLabelValue.height)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(contentAlignment = Alignment.CenterStart, content = left)
        Box(contentAlignment = Alignment.CenterEnd, content = right)
    }
}
