package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import com.airalo.example.offer.domain.entity.Operator
import com.airalo.example.offer.presentation.ui.token.Color
import com.airalo.example.offer.presentation.ui.token.Shape
import com.airalo.example.offer.presentation.ui.token.Size

@ExperimentalCoilApi
@Composable
fun OperatorLogo(operator: Operator) {
    Box(
        modifier = Modifier
            .height(Size.Surface.operatorLogo.height)
            .width(Size.Surface.operatorLogo.width)
            .background(Color.transparent, shape = Shape.large),
    ) {
        AsyncImage(
            model = operator.logo.url,
            error = ColorPainter(Color.transparent),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
