package com.airalo.example.offer.presentation.ui.token

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.airalo.example.offer.R

private val IbmPlexSans = FontFamily(
    Font(R.font.ibm_plex_sans_medium, FontWeight.Medium),
    Font(R.font.ibm_plex_sans_semibold, FontWeight.SemiBold),
)

object Font {
    val screenTitle = TextStyle(
        fontFamily = IbmPlexSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 27.sp,
        lineHeight = 32.sp,
        letterSpacing = (-0.5).sp,
        color = Color.charcoalGray,
    )

    val sectionTitle = TextStyle(
        fontFamily = IbmPlexSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 19.sp,
        lineHeight = 22.sp,
        letterSpacing = (-0.2).sp,
        color = Color.charcoalGray,
    )

    val offerSubtitle = TextStyle(
        fontFamily = IbmPlexSans,
        fontWeight = FontWeight.Medium,
        fontSize = 13.sp,
        lineHeight = 15.sp,
        letterSpacing = 0.sp,
        color = Color.charcoalGray,
    )

    val overviewCountryName = TextStyle(
        fontFamily = IbmPlexSans,
        fontWeight = FontWeight.Medium,
        fontSize = 15.sp,
        lineHeight = 20.sp,
        color = Color.charcoalGray,
    )

    val offerDetailLabel = TextStyle(
        fontFamily = IbmPlexSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 14.sp,
        letterSpacing = 1.sp,
        color = Color.charcoalGray,
    )

    val offerDetailValue = TextStyle(
        fontFamily = IbmPlexSans,
        fontWeight = FontWeight.Medium,
        fontSize = 17.sp,
        lineHeight = 20.sp,
        letterSpacing = (-0.1).sp,
        color = Color.charcoalGray,
    )

    val offerButtonLabel = TextStyle(
        fontFamily = IbmPlexSans,
        fontWeight = FontWeight.SemiBold,
        fontSize = 11.sp,
        lineHeight = 11.sp,
        letterSpacing = 1.sp,
        color = Color.charcoalGray,
    )
}
