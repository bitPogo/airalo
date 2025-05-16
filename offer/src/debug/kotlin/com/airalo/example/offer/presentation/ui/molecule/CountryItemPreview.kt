package com.airalo.example.offer.presentation.ui.molecule

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.entity.CountryFlagUri
import com.airalo.example.offer.domain.entity.Id

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CountryPreview() {
    Box(
        modifier = Modifier.padding(20.dp),
    ) {
        CountryItem(
            country = Country(
                id = Id(1),
                name = "Germany",
                flag = CountryFlagUri("https://cdn.airalo.com/images/473ba88c-547f-447e-970a-d52ba3748077.png"),
            ),
        ) { println("Clicked") }
    }
}
