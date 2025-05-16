package com.airalo.example.offer.presentation.ui.organism

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.presentation.ui.atom.MVerticalSpace
import com.airalo.example.offer.presentation.ui.atom.XSVerticalSpace
import com.airalo.example.offer.presentation.ui.atom.XXLVerticalSpace
import com.airalo.example.offer.presentation.ui.command.LoadOfferPackageForCountryCommand
import com.airalo.example.offer.presentation.ui.molecule.CountryItem
import com.airalo.example.offer.presentation.ui.molecule.CountryListHeader
import com.airalo.example.offer.presentation.ui.token.Spacing
import com.airalo.example.offer.presentation.viewmodel.OfferCommandExecutor
import com.airalo.example.offer.presentation.viewmodel.OfferCommandReceiver

@Composable
fun CountryList(
    countries: List<Country>,
    router: NavController,
    executor: OfferCommandExecutor<out OfferCommandReceiver>,
) {
    val listState = rememberLazyListState()
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            state = listState,
            modifier = Modifier.padding(
                start = Spacing.m,
                end = Spacing.m,
                bottom = Spacing.l,
            ),
        ) {
            item {
                XXLVerticalSpace()
                CountryListHeader()
                MVerticalSpace()
            }
            items(countries.size) { index ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CountryItem(countries[index]) {
                        executor(LoadOfferPackageForCountryCommand(countries[index].id, router))
                    }
                    XSVerticalSpace()
                }
            }
        }
    }
}
