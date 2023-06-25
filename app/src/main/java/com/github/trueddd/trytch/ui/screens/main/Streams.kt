package com.github.trueddd.trytch.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.github.trueddd.trytch.ui.buildImageRequest
import com.github.trueddd.trytch.ui.isLandscape
import com.github.trueddd.trytch.ui.modifyIf
import com.github.trueddd.trytch.ui.theme.AppTheme
import com.github.trueddd.trytch.ui.widgets.StreamTags
import com.github.trueddd.twitch.data.Stream
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList

private val StreamFieldHorizontalPadding = 4.dp

@Composable
private fun StreamerName(
    name: String,
    color: Color = AppTheme.AccentText,
    fontWeight: FontWeight = FontWeight.Bold,
    fontSize: TextUnit = 16.sp,
) {
    Text(
        text = name,
        fontSize = fontSize,
        maxLines = 1,
        color = color,
        fontWeight = fontWeight,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = StreamFieldHorizontalPadding)
    )
}

@Composable
private fun StreamTitle(
    title: String,
    color: Color = AppTheme.PrimaryText,
) {
    Text(
        text = title,
        fontSize = 14.sp,
        color = color,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = StreamFieldHorizontalPadding)
    )
}

@Composable
private fun StreamInfo(
    value: String,
    color: Color = AppTheme.PrimaryText,
    fontSize: TextUnit = 14.sp,
) {
    Text(
        text = value,
        fontSize = fontSize,
        color = color,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = StreamFieldHorizontalPadding)
    )
}

@Composable
private fun BoxScope.StreamViewers(viewersCountText: String) {
    Box(
        modifier = Modifier
            .padding(4.dp)
            .background(AppTheme.Primary, RoundedCornerShape(4.dp))
            .align(Alignment.BottomEnd)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            modifier = Modifier
                .padding(horizontal = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(6.dp)
                    .background(AppTheme.Secondary, CircleShape)
            )
            Text(
                text = viewersCountText,
                color = AppTheme.PrimaryText,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
private fun StreamPreview(
    stream: Stream,
    rounded: Boolean = false,
) {
    AsyncImage(
        model = buildImageRequest(stream.getThumbnailUrl(width = 320, height = 180)),
        contentDescription = "${stream.userName} stream thumbnail",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .fillMaxSize()
            .modifyIf(rounded) {
                clip(RoundedCornerShape(8.dp))
            }
    )
}

@Preview(
    widthDp = 450,
    heightDp = 100,
    showBackground = true,
    backgroundColor = 0xFF1D1C1D,
)
@Composable
private fun Stream(
    @PreviewParameter(StreamParameters::class)
    stream: Stream,
    onStreamClicked: (Stream) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .background(AppTheme.Primary)
            .clickable { onStreamClicked(stream) }
    ) {
        Box(
            modifier = Modifier
                .padding(start = 8.dp)
                .weight(2f)
                .aspectRatio(16 / 9f)
        ) {
            StreamPreview(stream)
            StreamViewers(viewersCountText = stream.shortenedViewerCount)
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(3f)
                .padding(start = 8.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                StreamerName(
                    stream.userName,
                    color = AppTheme.AccentText,
                    fontWeight = FontWeight.Normal,
                    fontSize = 18.sp,
                )
                StreamTitle(
                    stream.title,
                    color = AppTheme.PrimaryText,
                )
                StreamInfo(
                    stream.gameName,
                    color = AppTheme.PrimaryTextDark,
                    fontSize = 14.sp,
                )
                StreamTags(
                    stream.tags.toImmutableList(),
                    contentSpacing = StreamFieldHorizontalPadding,
                )
            }
        }
    }
}

@Composable
fun Streams(
    streams: ImmutableList<Stream>,
    modifier: Modifier = Modifier,
    onStreamClicked: (Stream) -> Unit,
) {
    val isLandscape = LocalConfiguration.current.isLandscape
    LazyVerticalGrid(
        columns = GridCells.Fixed(if (isLandscape) 2 else 1),
        contentPadding = PaddingValues(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier,
    ) {
        items(streams) { stream ->
            Stream(
                stream = stream,
                onStreamClicked = onStreamClicked,
            )
        }
    }
}

class StreamParameters : PreviewParameterProvider<Stream> {
    override val values: Sequence<Stream>
        get() = sequenceOf(Stream.test())
}
