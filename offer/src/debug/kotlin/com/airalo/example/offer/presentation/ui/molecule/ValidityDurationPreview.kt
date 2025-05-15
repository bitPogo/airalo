package com.airalo.example.offer.presentation.ui.molecule

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.airalo.example.offer.domain.entity.Validity

@Preview(showBackground = true)
@Composable
fun ValidityDurationPreview() {
    ValidityDuration(Validity("1 day"))
}
