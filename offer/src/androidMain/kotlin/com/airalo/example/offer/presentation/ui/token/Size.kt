package com.airalo.example.offer.presentation.ui.token

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class Dimension internal constructor(
    val width: Dp,
    val height: Dp,
)

object Size {
    object Surface {
        val countryFlag = Dimension(
            height = 28.dp,
            width = 37.dp,
        )
        val trailingArrow = Dimension(
            width = 7.dp,
            height = 12.dp,
        )
        val icon = Dimension(
            width = 22.dp,
            height = 22.dp,
        )
    }

    val surface = Surface
}
