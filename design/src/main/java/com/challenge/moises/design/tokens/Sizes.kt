package com.challenge.moises.design.tokens

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ButtonSizes(val small: Dp, val medium: Dp, val large: Dp, val extraLarge: Dp)
data class IconSizes(val small: Dp, val large: Dp, val larger: Dp, val extraLarge: Dp, val ultraLarge: Dp)
data class SpinnerStrokes(val thin: Dp, val thick: Dp)

val MoisesButtonSizes = ButtonSizes(
    small = 32.dp,
    medium = 40.dp,
    large = 48.dp,
    extraLarge = 56.dp
)

val MoisesIconSizes = IconSizes(
    small = 16.dp,
    large = 24.dp,
    larger = 26.dp,
    extraLarge = 32.dp,
    ultraLarge = 48.dp
)

val MoisesSpinnerStrokes = SpinnerStrokes(
    thin = 1.dp,
    thick = 2.dp
)
