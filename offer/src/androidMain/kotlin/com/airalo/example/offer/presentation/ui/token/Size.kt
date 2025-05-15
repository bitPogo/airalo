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
        val innerIcon = Dimension(
            width = 16.dp,
            height = 16.dp,
        )
        val icon = Dimension(
            width = 22.dp,
            height = 22.dp,
        )
        val countryItem = Dimension(
            width = 335.dp,
            height = 55.dp,
        )
        val countryItemLabel = Dimension(
            width = 221.dp,
            height = 20.dp,
        )
        val countryListHeader = Dimension(
            width = 156.dp,
            height = 22.dp,
        )
        val screenHeaderTitle = Dimension(
            width = 375.dp,
            height = 32.dp,
        )
        val screenHeader = Dimension(
            width = 375.dp,
            height = 111.dp,
        )
        val operatorLogo = Dimension(
            width = 140.dp,
            height = 88.dp,
        )
        val detailLabelValue = Dimension(
            width = 335.dp,
            height = 58.dp,
        )
    }

    val surface = Surface
}
