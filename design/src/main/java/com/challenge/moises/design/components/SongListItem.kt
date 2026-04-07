package com.challenge.moises.design.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.challenge.moises.design.R
import com.challenge.moises.design.tokens.MoisesIconSizes
import com.challenge.moises.design.tokens.MoisesSpacings

@Composable
fun SongListItem(
    modifier: Modifier = Modifier,
    title: String,
    subtitle: String,
    imageUrl: String? = null,
    imageEnabled: Boolean = true,
    onClick: () -> Unit = {},
    trailingIcon: @Composable (() -> Unit)? = {
        IconButton(onClick = { /* More actions */ }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = stringResource(R.string.more_content_description),
                tint = Color.Gray,
                modifier = Modifier.size(MoisesIconSizes.large)
            )
        }
    }
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = MoisesSpacings.small),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MoisesSpacings.medium)
    ) {
        if (imageEnabled) {
            ImageIcon(imageUrl)
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }

        if (trailingIcon != null) {
            trailingIcon()
        }
    }
}

@Preview
@Composable
fun SongListItemPreview() {
    SongListItem(
        title = "Take On Me",
        subtitle = "a-ha",
        imageUrl = null,
        onClick = {},
    )
}
