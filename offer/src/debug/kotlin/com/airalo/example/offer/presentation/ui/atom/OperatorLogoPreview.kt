package com.airalo.example.offer.presentation.ui.atom

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.airalo.example.offer.domain.entity.Operator

@Preview(showBackground = true)
@Composable
fun RemoteOperatorLogo() {
    OperatorLogo(
        Operator(
            name = "",
            logo = com.airalo.example.offer.domain.entity.OperatorLogoUrl("https://cdn.airalo.com/images/d71dd812-9787-408e-a362-85346756762c.jpg"),
        ),
    )
}

@Preview(showBackground = true)
@Composable
fun FallbackOperatorLogo() {
    OperatorLogo(
        Operator(
            name = "",
            logo = com.airalo.example.offer.domain.entity.OperatorLogoUrl("https://example.com/noimage.jpg"),
        ),
    )
}
