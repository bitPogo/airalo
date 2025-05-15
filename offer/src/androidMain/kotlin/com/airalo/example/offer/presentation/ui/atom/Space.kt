package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airalo.example.offer.presentation.ui.token.Spacing

@Composable
fun SHorizontalSpace() {
    Spacer(
        modifier = Modifier
            .fillMaxHeight()
            .width(Spacing.s),
    )
}

@Composable
fun XSVerticalSpace() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(Spacing.xs),
    )
}

@Composable
fun MVerticalSpace() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(Spacing.m),
    )
}

@Composable
fun XLVerticalSpace() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(Spacing.xl),
    )
}

@Composable
fun XXLVerticalSpace() {
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .height(Spacing.xxl),
    )
}
