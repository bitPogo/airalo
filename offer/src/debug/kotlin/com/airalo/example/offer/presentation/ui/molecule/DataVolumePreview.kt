package com.airalo.example.offer.presentation.ui.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.airalo.example.offer.domain.entity.Volume

@Preview(showBackground = true)
@Composable
fun DataIconPreview() {
    DataVolume(Volume("1 GB"))
}
