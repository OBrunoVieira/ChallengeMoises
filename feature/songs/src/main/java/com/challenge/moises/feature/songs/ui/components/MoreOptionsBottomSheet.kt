package com.challenge.moises.feature.songs.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.challenge.moises.core.network.domain.models.Song
import com.challenge.moises.design.components.SongListItem
import com.challenge.moises.design.tokens.MoisesSpacings
import com.challenge.moises.design.R as DesignR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreOptionsBottomSheet(
    song: Song,
    onDismissRequest: () -> Unit,
    onAlbumClick: (String) -> Unit,
    onRemoveFromRecent: (() -> Unit)? = null
) {
    ModalBottomSheet(
        onDismissRequest = onDismissRequest,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = Color.DarkGray
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            SongListItem(
                modifier = Modifier
                    .padding(horizontal = MoisesSpacings.large),
                title = song.title,
                subtitle = song.artistName,
                imageUrl = song.artworkUrl,
                trailingIcon = null
            )

            Spacer(Modifier.height(MoisesSpacings.medium))

            if (!song.collectionId.isNullOrEmpty()) {
                OptionListItem(
                    label = stringResource(DesignR.string.view_album),
                    icon = Icons.Default.Album,
                    onClick = {
                        onDismissRequest()
                        song.collectionId?.let { onAlbumClick(it) }
                    }
                )
            }

            if (onRemoveFromRecent != null) {
                OptionListItem(
                    label = stringResource(DesignR.string.remove_from_recent),
                    icon = Icons.Default.DeleteOutline,
                    onClick = {
                        onDismissRequest()
                        onRemoveFromRecent()
                    }
                )
            }
        }
    }
}

@Composable
private fun OptionListItem(
    label: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    ListItem(
        headlineContent = {
            Text(label, color = Color.White)
        },
        leadingContent = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = Color.White
            )
        },
        modifier = Modifier.clickable(onClick = onClick),
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )
}
