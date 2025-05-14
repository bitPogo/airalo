package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.airalo.example.offer.domain.entity.CountryFlagUri

@Preview(showBackground = true)
@Composable
fun RemoteCountryFlag() {
    CountryFlag(CountryFlagUri("https://cdn.airalo.com/images/473ba88c-547f-447e-970a-d52ba3748077.png"))
}

@Preview(showBackground = true)
@Composable
fun DefaultCountryFlag() {
    CountryFlag(CountryFlagUri("https://cdn.airalo.com/nope"))
}
