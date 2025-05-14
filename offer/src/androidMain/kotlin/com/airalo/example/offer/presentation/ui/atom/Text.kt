package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.airalo.example.offer.presentation.ui.token.Font.overviewCountryName

@Composable
fun CountryName(content: String) {
    Text(
        content,
        textAlign = TextAlign.Left,
        style = overviewCountryName,
        modifier = Modifier
            .fillMaxSize(),
    )
}
