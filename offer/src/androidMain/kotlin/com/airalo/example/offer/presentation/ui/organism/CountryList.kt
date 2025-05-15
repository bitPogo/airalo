package com.airalo.example.offer.presentation.ui.organism

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.airalo.example.offer.domain.entity.Country
import com.airalo.example.offer.presentation.ui.atom.XSVerticalSpace
import com.airalo.example.offer.presentation.ui.molecule.CountryItem
import com.airalo.example.offer.presentation.ui.molecule.CountryListHeader
import com.airalo.example.offer.presentation.ui.token.Color
import com.airalo.example.offer.presentation.ui.token.Spacing

@Composable
fun CountryList(countries: List<Country>) {
    val listState = rememberLazyListState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.lightGray),
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
                CountryListHeader()
            }
            items(countries.size) { index ->
                CountryItem(countries[index])
                XSVerticalSpace()
            }
        }
    }
}
