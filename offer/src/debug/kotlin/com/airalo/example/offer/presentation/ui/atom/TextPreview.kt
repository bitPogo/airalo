package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun CountryNamePreview() {
    Box {
        CountryName("Germany")
    }
}

@Preview(showBackground = true)
@Composable
fun SectionTitlePreview() {
    Box {
        SectionTitle("Yes!", contentDescription = "No!")
    }
}

@Preview(showBackground = true)
@Composable
fun ScreenTitlePreview() {
    Box {
        SectionTitle("Yes!", contentDescription = "No!")
    }
}
