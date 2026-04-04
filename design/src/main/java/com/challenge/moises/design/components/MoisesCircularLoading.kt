package com.challenge.moises.design.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.challenge.moises.design.tokens.MoisesIconSizes
import com.challenge.moises.design.tokens.MoisesSpinnerStrokes

@Composable
fun MoisesCircularLoading(
    modifier: Modifier = Modifier,
    color: Color = Color.White,
    strokeWidth: Dp = MoisesSpinnerStrokes.thick,
    size: Dp = MoisesIconSizes.extraLarge
) {
    CircularProgressIndicator(
        color = color,
        strokeWidth = strokeWidth,
        modifier = modifier.size(size)
    )
}
