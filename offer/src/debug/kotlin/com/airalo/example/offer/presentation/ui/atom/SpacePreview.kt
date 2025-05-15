package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Preview(showBackground = true)
@Composable
fun SHorizontalSpacePreview() {
    Box(
        modifier = Modifier.border(2.dp, Color.Black).height(20.dp),
    ) {
        SHorizontalSpace()
    }
}

@Preview(showBackground = true)
@Composable
fun XSVerticalSpacePreview() {
    Box(
        modifier = Modifier.border(2.dp, Color.Black).width(20.dp),
    ) {
        XSVerticalSpace()
    }
}

@Preview(showBackground = true)
@Composable
fun MVerticalSpacePreview() {
    Box(
        modifier = Modifier.border(2.dp, Color.Black).width(20.dp),
    ) {
        MVerticalSpace()
    }
}

@Preview(showBackground = true)
@Composable
fun XLVerticalSpacePreview() {
    Box(
        modifier = Modifier.border(2.dp, Color.Black).width(20.dp),
    ) {
        XLVerticalSpace()
    }
}
