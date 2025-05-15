package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun OfferDetailsActionSlotPreview() {
    Box(modifier = Modifier.border(1.dp, color = Color.Black)) {
        OfferDetailActionSlot { Text("Jo") }
    }
}
