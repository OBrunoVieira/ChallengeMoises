package com.challenge.moises.design.tokens

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val MoisesColorScheme = darkColorScheme(
    primary = White,
    secondary = DarkGrey,
    tertiary = Red,
    background = Black,
    surface = DarkGrey,
    onPrimary = Black,
    onSecondary = White,
    onTertiary = White,
    onBackground = White,
    onSurface = White,
    onSurfaceVariant = TextGrey
)

@Composable
fun MoisesTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = MoisesColorScheme,
        content = content
    )
}
