package com.challenge.moises.design.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.challenge.moises.design.tokens.MoisesIconSizes

@Composable
fun CircledImageIcon(
    imageUrl: String? = null,
    size: Dp = 48.dp
) {
    ImageIcon(
        imageUrl = imageUrl,
        size = size,
        shape = CircleShape
    )
}

@Composable
fun ImageIcon(
    imageUrl: String? = null,
    size: Dp = 48.dp,
    shape: Shape = RoundedCornerShape(10.dp),
) {
    Box(
        modifier = Modifier
            .size(size)
            .clip(shape)
            .background(Color(0xFF1C1C1E)),
        contentAlignment = Alignment.Center
    ) {
        if (imageUrl != null) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Icon(
                imageVector = Icons.Default.MusicNote,
                contentDescription = null,
                tint = Color.Gray,
                modifier = Modifier.size(MoisesIconSizes.large)
            )
        }
    }
}