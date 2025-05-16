package com.airalo.example.offer.presentation.ui.molecule

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.presentation.ui.atom.CountryFlag
import com.airalo.example.offer.presentation.ui.atom.CountryName
import com.airalo.example.offer.presentation.ui.atom.SHorizontalSpace
import com.airalo.example.offer.presentation.ui.atom.TrailingArrow
import com.airalo.example.offer.presentation.ui.shadow
import com.airalo.example.offer.presentation.ui.token.Color
import com.airalo.example.offer.presentation.ui.token.Shape
import com.airalo.example.offer.presentation.ui.token.Size
import com.airalo.example.offer.presentation.ui.token.Spacing

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun CountryItem(
    country: Country,
    onClick: Function0<Unit>,
) {
    Box(
        modifier = Modifier.shadow()
            .clickable { onClick() },
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .semantics {
                    contentDescription = country.name
                }
                .width(Size.surface.countryItem.width)
                .height(Size.surface.countryItem.height)
                .background(Color.white, shape = Shape.medium),
        ) {
            val textWidth = maxWidth - Spacing.m * 2 - Size.surface.countryFlag.width - Spacing.s - Size.surface.trailingArrow.width

            Row(
                modifier = Modifier
                    .padding(
                        start = Spacing.m,
                        end = Spacing.m,
                    )
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CountryFlag(country.flag)
                SHorizontalSpace()
                Box(
                    modifier = Modifier
                        .height(Size.surface.countryItemLabel.height)
                        .width(textWidth),
                ) {
                    CountryName(country.name)
                }
                TrailingArrow()
            }
        }
    }
}
