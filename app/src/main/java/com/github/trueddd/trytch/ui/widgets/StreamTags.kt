package com.github.trueddd.trytch.ui.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.trueddd.trytch.ui.theme.AppTheme
import kotlinx.collections.immutable.ImmutableList

@Composable
fun StreamTags(
    tags: ImmutableList<String>,
    modifier: Modifier = Modifier,
    contentSpacing: Dp = 4.dp,
) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(contentSpacing),
        contentPadding = PaddingValues(horizontal = contentSpacing),
        modifier = modifier
            .height(16.dp)
    ) {
        items(tags) { tag ->
            Box(
                modifier = Modifier
                    .background(AppTheme.SecondaryText, RoundedCornerShape(8.dp))
            ) {
                Text(
                    text = tag,
                    color = AppTheme.AccentText,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .padding(horizontal = contentSpacing)
                )
            }
        }
    }
}
