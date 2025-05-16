package com.airalo.example.offer.presentation.ui.organism

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.airalo.example.offer.domain.entity.Offer
import com.airalo.example.offer.presentation.ui.atom.MVerticalSpace
import com.airalo.example.offer.presentation.ui.atom.XLVerticalSpace
import com.airalo.example.offer.presentation.ui.molecule.OfferPackage
import com.airalo.example.offer.presentation.ui.token.Spacing

@Composable
fun OfferList(offers: List<Offer>) {
    val listState = rememberLazyListState()
    Box(
        modifier = Modifier.fillMaxSize(),
    ) {
        LazyColumn(
            state = listState,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(
                start = Spacing.m,
                end = Spacing.m,
                bottom = Spacing.l,
            ),
        ) {
            item {
                XLVerticalSpace()
            }

            items(offers.size) { index ->
                OfferPackage(offers[index])
                MVerticalSpace()
            }
        }
    }
}
