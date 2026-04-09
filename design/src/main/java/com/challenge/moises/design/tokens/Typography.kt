package com.challenge.moises.design.tokens

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.challenge.moises.design.R

private val ArticulatCF = FontFamily(
    Font(R.font.articulat_cf_regular, FontWeight.W400),
    Font(R.font.articulat_cf_medium, FontWeight.W500),
    Font(R.font.articulat_cf_demi_bold, FontWeight.W600)
)

val MoisesTypography = Typography(
    headlineMedium = TextStyle(
        fontFamily = ArticulatCF,
        fontWeight = FontWeight.W600,
        fontSize = 24.sp,
        color = White
    ),
    titleMedium = TextStyle(
        fontFamily = ArticulatCF,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        color = White
    ),
    bodySmall = TextStyle(
        fontFamily = ArticulatCF,
        fontWeight = FontWeight.W500,
        fontSize = 12.sp,
        color = White_70
    ),
    displayLarge = TextStyle(
        fontFamily = ArticulatCF,
        fontWeight = FontWeight.W600,
        fontSize = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = ArticulatCF,
        fontWeight = FontWeight.W600,
        fontSize = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = ArticulatCF,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    labelSmall = TextStyle(
        fontFamily = ArticulatCF,
        fontWeight = FontWeight.W600,
        fontSize = 9.sp
    )
)
