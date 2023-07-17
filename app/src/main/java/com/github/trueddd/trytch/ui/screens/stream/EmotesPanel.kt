package com.github.trueddd.trytch.ui.screens.stream

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.trueddd.trytch.R
import com.github.trueddd.trytch.ui.CoilImage
import com.github.trueddd.trytch.ui.buildImageRequest
import com.github.trueddd.trytch.ui.screens.stream.chat.EmotesPanelState
import com.github.trueddd.trytch.ui.theme.AppTheme
import com.github.trueddd.twitch.data.Emote
import com.github.trueddd.twitch.emotes.EmotesProvider

@Preview
@Composable
private fun EmotesPanelPreview() {
    EmotesPanel(
        emotesPanelState = EmotesPanelState.test(),
    )
}

@Preview
@Composable
private fun EmptyEmotesPanelPreview() {
    EmotesPanel(
        emotesPanelState = EmotesPanelState.test(),
    )
}

private const val EmotesRows = 5
private val EmotePreviewHeight = 24.dp
private val EmotesSpacing = 4.dp
private val EmotesSidePadding = 8.dp

@Composable
private fun EmoteProviderTabHeader(
    name: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .background(
                color = if (selected) AppTheme.Accent else AppTheme.SecondaryText,
                shape = RoundedCornerShape(4.dp),
            )
            .clickable(onClick = onClick)
    ) {
        Text(
            text = name.uppercase(),
            color = if (selected) AppTheme.SecondaryText else AppTheme.PrimaryText,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .padding(horizontal = 4.dp, vertical = 2.dp)
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EmotesPanel(
    emotesPanelState: EmotesPanelState,
    modifier: Modifier = Modifier,
    onEmotesTabChanged: (Emote.Provider) -> Unit = {},
    onSearchToggled: () -> Unit = {},
    onSearchTextChanged: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(AppTheme.Primary, RoundedCornerShape(8.dp))
            .border(1.dp, AppTheme.PrimaryTextDark, RoundedCornerShape(8.dp))
    ) {
        var emotesSearchText by remember { mutableStateOf("") }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (emotesPanelState.searchEnabled) {
                BasicTextField(
                    value = emotesSearchText,
                    onValueChange = {
                        emotesSearchText = it
                        onSearchTextChanged(it)
                    },
                    singleLine = true,
                    textStyle = TextStyle.Default.copy(
                        fontSize = 14.sp,
                        color = AppTheme.PrimaryText,
                    ),
                    cursorBrush = SolidColor(AppTheme.Accent),
                    decorationBox = { textField ->
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                                .fillMaxWidth()
                        ) {
                            textField()
                            if (emotesSearchText.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.chat_search_emote),
                                    fontSize = 14.sp,
                                    color = AppTheme.PrimaryTextDark,
                                    modifier = Modifier
                                        .align(Alignment.CenterStart)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .weight(1f)
                        .background(AppTheme.SecondaryText, RoundedCornerShape(8.dp))
                )
            } else {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .weight(1f)
                ) {
                    items(EmotesProvider.AllEmoteProviders) { provider ->
                        EmoteProviderTabHeader(
                            name = provider.value,
                            selected = emotesPanelState.selectedProvider == provider,
                            onClick = { onEmotesTabChanged(provider) },
                        )
                    }
                }
            }
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search emotes",
                tint = if (emotesPanelState.searchEnabled) AppTheme.Primary else AppTheme.PrimaryText,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(26.dp)
                    .background(
                        color = if (emotesPanelState.searchEnabled) AppTheme.Accent else Color.Transparent,
                        shape = CircleShape,
                    )
                    .padding(2.dp)
                    .clickable(onClick = onSearchToggled)
            )
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(AppTheme.PrimaryTextDark)
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth()
                .height(EmotesSpacing * (EmotesRows - 1) + EmotesSidePadding * 2 + EmotePreviewHeight * 5)
        ) {
            LazyHorizontalStaggeredGrid(
                rows = StaggeredGridCells.Fixed(EmotesRows),
                contentPadding = PaddingValues(EmotesSidePadding),
                verticalArrangement = Arrangement.spacedBy(EmotesSpacing),
                horizontalArrangement = Arrangement.spacedBy(EmotesSpacing),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(emotesPanelState.emotes) {
                    EmotePreview(it)
                }
            }
            if (emotesPanelState.emotes.isEmpty()) {
                Text(
                    text = "No emotes found",
                    color = AppTheme.PrimaryText,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                )
            }
        }
    }
}

@Preview
@Composable
private fun EmotePreview(
    emote: Emote = Emote.test(),
) {
    CoilImage(
        model = buildImageRequest(emote.versions.first().url),
        contentDescription = emote.name,
        modifier = Modifier
            .size(EmotePreviewHeight)
            .clickable { Log.d("Emote", "Clicked on ${emote.name}") }
    )
}
