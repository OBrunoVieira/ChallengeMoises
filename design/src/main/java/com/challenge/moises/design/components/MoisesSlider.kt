package com.challenge.moises.design.components

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.challenge.moises.design.tokens.MoisesSpacings

@Composable
fun MoisesSlider(
    modifier: Modifier = Modifier,
    currentPosition: Long,
    duration: Long,
    onSeek: (Long) -> Unit = {}
) {
    var dragValue by remember { mutableStateOf<Float?>(null) }
    val displayValue = dragValue ?: currentPosition.toFloat()

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(MoisesSpacings.small)
    ) {
        Slider(
            value = displayValue,
            valueRange = 0f..duration.toFloat().coerceAtLeast(1f),
            onValueChange = { dragValue = it },
            onValueChangeFinished = {
                dragValue?.let { onSeek(it.toLong()) }
                dragValue = null
            },
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary,
                inactiveTrackColor = Color.White.copy(alpha = 0.3f)
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = displayValue.toLong().toElapsedTime(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
            Text(
                text = duration.toElapsedTime(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.White
            )
        }
    }
}

/**
 * Formats an elapsed time in the form "MM:SS" or "H:MM:SS" for display on the call-in-progress
 * screen.
 */
private fun Long.toElapsedTime(): String {
    return DateUtils.formatElapsedTime(this / 1000)
}

@Preview
@Composable
fun MoisesSliderPreview() {
    MoisesSlider(currentPosition = 0, duration = 1000 * 30)
}