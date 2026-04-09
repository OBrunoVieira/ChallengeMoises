package com.challenge.moises.design.components

import android.text.format.DateUtils
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpSize
import com.challenge.moises.design.tokens.MoisesSpacings

@OptIn(ExperimentalMaterial3Api::class)
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
        modifier = modifier.fillMaxWidth()
    ) {
        Slider(
            value = displayValue,
            valueRange = 0f..duration.toFloat().coerceAtLeast(1f),
            onValueChange = { dragValue = it },
            onValueChangeFinished = {
                dragValue?.let { onSeek(it.toLong()) }
                dragValue = null
            },
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = remember { MutableInteractionSource() },
                    thumbSize = DpSize(12.dp, 12.dp),
                    colors = SliderDefaults.colors(thumbColor = Color.White)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(
                    sliderState = sliderState,
                    modifier = Modifier.height(4.dp),
                    colors = SliderDefaults.colors(
                        thumbColor = MaterialTheme.colorScheme.primary,
                        activeTrackColor = MaterialTheme.colorScheme.primary,
                        inactiveTrackColor = Color.White.copy(alpha = 0.3f)
                    )
                )
            }
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
            val remainingTime = duration - displayValue.toLong()
            Text(
                text = "-${remainingTime.coerceAtLeast(0L).toElapsedTime()}",
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
    MoisesSlider(currentPosition = 0, duration = 1000 * 30)
}