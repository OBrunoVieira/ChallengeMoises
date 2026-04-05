package com.challenge.moises.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.challenge.moises.design.tokens.MoisesSpacings
import android.text.format.DateUtils
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun MoisesSlider(
    currentPosition: Long,
    duration: Long,
    onSeek: (Long) -> Unit = {},
    modifier: Modifier = Modifier
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
                inactiveTrackColor = Color.Gray.copy(alpha = 0.3f)
            )
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = displayValue.toLong().toElapsedTime(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
            )
            Text(
                text = duration.toElapsedTime(),
                style = MaterialTheme.typography.labelSmall,
                color = Color.Gray
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
    MoisesSlider(0, 1000*30,)
}