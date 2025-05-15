package com.airalo.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TransparentPrimaryColorScheme = lightColorScheme(
    primary = Color.White,
    background = Color.White,
)

@Composable
fun AiraloTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = TransparentPrimaryColorScheme,
        content = content
    )
} 
