package com.airalo.example.offer.presentation.ui.molecule

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airalo.example.offer.presentation.ui.atom.GoBackButton
import com.airalo.example.offer.presentation.ui.atom.ScreenTitle
import com.airalo.example.offer.presentation.ui.token.Color
import com.airalo.example.offer.presentation.ui.token.Size
import com.airalo.example.offer.presentation.ui.token.Spacing

@Composable
fun ScreenHeader(
    screenTitle: String,
    action: Function0<Unit>? = null,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(Size.Surface.screenHeader.height)
            .background(color = Color.white),
    ) {
        Column(
            verticalArrangement = Arrangement.SpaceEvenly,
        ) {
            action?.let {
                GoBackButton(action)
            }
            Box(
                modifier = Modifier
                    .padding(bottom = Spacing.m, start = Spacing.m)
                    .height(Size.Surface.screenHeaderTitle.height),
                contentAlignment = Alignment.BottomStart,
            ) {
                ScreenTitle(screenTitle)
            }
        }
    }
}
