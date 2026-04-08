package com.challenge.moises.feature.songs.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Album
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
    onAlbumClick: (String) -> Unit
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

            HorizontalDivider(color = Color.Gray.copy(alpha = 0.5f))

            if (!song.collectionId.isNullOrEmpty()) {
                ListItem(
                    headlineContent = {
                        Text(
                            stringResource(DesignR.string.go_to_album),
                            color = Color.White
                        )
                    },
                    leadingContent = {
                        Icon(
                            Icons.Default.Album,
                            contentDescription = null,
                            tint = Color.White
                        )
                    },
                    modifier = Modifier.clickable {
                        onDismissRequest()
                        song.collectionId?.let { onAlbumClick(it) }
                    },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent)
                )
            }
        }
    }
}
