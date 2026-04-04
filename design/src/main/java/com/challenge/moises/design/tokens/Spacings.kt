package com.challenge.moises.design.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Spacings(
    val grande: Dp,
    val extraLarge: Dp,
    val large: Dp,
    val medium: Dp,
    val mediumSmall: Dp,
    val small: Dp,
    val extraSmall: Dp,
    val divider: Dp
)

val MoisesSpacings = Spacings(
    grande = 48.dp,
    extraLarge = 32.dp,
    large = 24.dp,
    medium = 16.dp,
    mediumSmall = 12.dp,
    small = 8.dp,
    extraSmall = 4.dp,
    divider = 1.dp
)
