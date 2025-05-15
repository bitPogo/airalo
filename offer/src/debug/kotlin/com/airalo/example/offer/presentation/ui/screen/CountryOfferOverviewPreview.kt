@file:Suppress("ktlint:standard:max-line-length", "ktlint:standard:backing-property-naming")

package com.airalo.example.offer.presentation.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import com.airalo.example.command.Command
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.domain.entity.CountryFlagUri
import com.airalo.example.offer.domain.entity.Id
import com.airalo.example.offer.presentation.model.CountryOfferOverviewStateHolder
import com.airalo.example.offer.presentation.model.CountryOverviewUiState
import com.airalo.example.offer.presentation.viewmodel.LoadPopularOfferCountriesCommandReceiverContract
import com.airalo.example.offer.presentation.viewmodel.OfferCommandExecutor
import com.airalo.example.offer.presentation.viewmodel.OfferCommandReceiver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Preview(showBackground = true)
@Composable
fun CountryOfferOverviewPreview() {
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

    val viewModel = FakeOfferPreviewViewModel(countries)

    CountryOfferOverview(viewModel, viewModel)
}

private class FakeOfferPreviewViewModel(
    private val _countries: List<Country>,
) :
    CountryOfferOverviewStateHolder,
        OfferCommandExecutor<OfferCommandReceiver>,
        LoadPopularOfferCountriesCommandReceiverContract,
        ViewModel() {
    private val _state: MutableStateFlow<CountryOverviewUiState> = MutableStateFlow(CountryOverviewUiState.Initial)
    override val countries: StateFlow<CountryOverviewUiState> = _state.asStateFlow()

    override fun invoke(command: Command<in OfferCommandReceiver>) {
        command.invoke(this)
    }

    override fun loadPopularOfferCountries() {
        _state.update { CountryOverviewUiState.Success(_countries) }
    }
}
