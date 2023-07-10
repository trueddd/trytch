package com.github.trueddd.trytch.ui.screens.stream

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.github.trueddd.trytch.ui.theme.AppTheme
import com.github.trueddd.twitch.data.Emote
import com.github.trueddd.twitch.emotes.EmotesProvider
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

@Preview
@Composable
private fun EmotesPanelPreview() {
    EmotesPanel()
}

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

@Composable
fun EmotesPanel(
    modifier: Modifier = Modifier,
    providers: ImmutableList<Emote.Provider> = EmotesProvider.AllEmoteProviders.toImmutableList(),
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(AppTheme.Primary, RoundedCornerShape(8.dp))
            .border(1.dp, AppTheme.PrimaryTextDark, RoundedCornerShape(8.dp))
    ) {
        var selectedTab by remember { mutableStateOf(providers.first()) }
        var searchEnabled by remember { mutableStateOf(false) }
        var emotesSearchText by remember { mutableStateOf("") }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            if (searchEnabled) {
                BasicTextField(
                    value = emotesSearchText,
                    onValueChange = { emotesSearchText = it },
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
                    items(providers) { provider ->
                        EmoteProviderTabHeader(
                            name = provider.value,
                            selected = selectedTab == provider,
                            onClick = { selectedTab = provider },
                        )
                    }
                }
            }
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search emotes",
                tint = if (searchEnabled) AppTheme.Primary else AppTheme.PrimaryText,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(26.dp)
                    .background(
                        color = if (searchEnabled) AppTheme.Accent else Color.Transparent,
                        shape = CircleShape,
                    )
                    .padding(2.dp)
                    .clickable { searchEnabled = !searchEnabled }
            )
        }
        Box(
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .fillMaxWidth()
                .height(1.dp)
                .background(AppTheme.PrimaryTextDark)
        )
    }
}
