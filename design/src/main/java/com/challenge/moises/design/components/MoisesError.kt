package com.challenge.moises.design.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.challenge.moises.design.R
import com.challenge.moises.design.tokens.MoisesSpacings
import com.challenge.moises.design.tokens.White_70
import com.challenge.moises.design.tokens.annotations.MoisesPreviewScreenSizes
import com.challenge.moises.design.ui.models.MoisesErrorType

@Composable
fun MoisesError(
    type: MoisesErrorType,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    val title = when (type) {
        MoisesErrorType.INTERNET -> stringResource(R.string.error_internet_title)
        MoisesErrorType.SERVER -> stringResource(R.string.error_server_title)
    }
    val subtitle = when (type) {
        MoisesErrorType.INTERNET -> stringResource(R.string.error_internet_subtitle)
        MoisesErrorType.SERVER -> stringResource(R.string.error_server_subtitle)
    }
    val icon = when (type) {
        MoisesErrorType.INTERNET -> Icons.Default.CloudOff
        MoisesErrorType.SERVER -> Icons.Default.ErrorOutline
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(MoisesSpacings.large),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(80.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(Modifier.height(MoisesSpacings.medium))

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(MoisesSpacings.small))

        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            color = White_70,
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(MoisesSpacings.large))

        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = Color.Black
            )
        ) {
            Text(stringResource(R.string.error_retry_button))
        }
    }
}

@MoisesPreviewScreenSizes
@Composable
private fun MoisesErrorInternetPreview() {
    MoisesError(
        type = MoisesErrorType.INTERNET,
        onRetry = {}
    )
}

@MoisesPreviewScreenSizes
@Composable
private fun MoisesErrorServerPreview() {
    MoisesError(
        type = MoisesErrorType.SERVER,
        onRetry = {}
    )
}
