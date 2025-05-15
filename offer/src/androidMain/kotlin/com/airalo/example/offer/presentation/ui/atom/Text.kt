package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import com.airalo.example.offer.presentation.ui.token.Font
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

@Composable
fun SectionTitle(
    content: String,
    contentDescription: String,
) {
    Text(
        content,
        textAlign = TextAlign.Left,
        style = Font.sectionTitle,
        modifier = Modifier
            .fillMaxSize()
            .semantics {
                this.contentDescription = contentDescription
            },
    )
}

@Composable
fun ScreenTitle(content: String) {
    Text(
        content,
        textAlign = TextAlign.Left,
        style = Font.screenTitle,
        modifier = Modifier.fillMaxSize(),
    )
}


