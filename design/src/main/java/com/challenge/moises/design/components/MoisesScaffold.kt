package com.challenge.moises.design.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * Custom Scaffold that provides a consistent TopAppBar styling across all screens.
 *
 * @param title Title string shown in the TopAppBar.
 * @param onBackClick If non-null, a back arrow navigation icon is shown and this lambda is invoked on click.
 * @param containerColor Background color of the Scaffold. Defaults to [Color.Black].
 * @param topBarContainerColor Background color of the TopAppBar. Defaults to [Color.Transparent].
 * @param actions Optional trailing action icons in the TopAppBar.
 * @param titleContent Optional custom title composable. When provided, [title] is ignored.
 * @param content The screen body content, receiving PaddingValues from the Scaffold.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoisesScaffold(
    title: String = "",
    onBackClick: (() -> Unit)? = null,
    containerColor: Color = Color.Black,
    topBarContainerColor: Color = Color.Transparent,
    actions: @Composable RowScope.() -> Unit = {},
    titleContent: @Composable (() -> Unit)? = null,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier.statusBarsPadding(),
                title = {
                    titleContent?.invoke() ?: Text(
                        text = title,
                        style = MaterialTheme.typography.headlineMedium
                    )
                },
                navigationIcon = {
                    if (onBackClick != null) {
                        MoisesIconButton(
                            onClick = onBackClick,
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = actions,
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = topBarContainerColor,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
            )
        },
        containerColor = containerColor,
        content = content
    )
}
