package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import com.airalo.example.offer.presentation.ui.token.Color
import com.airalo.example.offer.presentation.ui.token.Size

@Composable
fun ValueDivider() {
    HorizontalDivider(
        color = Color.detailDivider,
        thickness = Size.border.xs,
    )
}
