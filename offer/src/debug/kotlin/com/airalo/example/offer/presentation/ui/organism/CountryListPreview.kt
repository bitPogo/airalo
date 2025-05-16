package com.airalo.example.offer.presentation.ui.organism

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.entity.CountryFlagUri
import com.airalo.example.offer.domain.entity.Id

@Preview(showBackground = true)
@Composable
fun CountryListPreview() {
    val countries = listOf(
        Country(
            id = Id(1),
            name = "United States",
            flag = CountryFlagUri("https://cdn.airalo.com/images/us_flag.png"),
        ),
        Country(
            id = Id(2),
            name = "Canada",
            flag = CountryFlagUri("https://cdn.airalo.com/images/ca_flag.png"),
        ),
        Country(
            id = Id(3),
            name = "Mexico",
            flag = CountryFlagUri("https://cdn.airalo.com/images/mx_flag.png"),
        ),
        Country(
            id = Id(4),
            name = "United Kingdom",
            flag = CountryFlagUri("https://cdn.airalo.com/images/uk_flag.png"),
        ),
        Country(
            id = Id(5),
            name = "Germany",
            flag = CountryFlagUri("https://cdn.airalo.com/images/de_flag.png"),
        ),
        Country(
            id = Id(6),
            name = "France",
            flag = CountryFlagUri("https://cdn.airalo.com/images/fr_flag.png"),
        ),
        Country(
            id = Id(7),
            name = "Japan",
            flag = CountryFlagUri("https://cdn.airalo.com/images/jp_flag.png"),
        ),
        Country(
            id = Id(8),
            name = "Australia",
            flag = CountryFlagUri("https://cdn.airalo.com/images/au_flag.png"),
        ),
        Country(
            id = Id(9),
            name = "Brazil",
            flag = CountryFlagUri("https://cdn.airalo.com/images/br_flag.png"),
        ),
        Country(
            id = Id(10),
            name = "India",
            flag = CountryFlagUri("https://cdn.airalo.com/images/in_flag.png"),
        ),
    )

    val navController = rememberNavController()

    CountryList(countries, navController) {}
}
