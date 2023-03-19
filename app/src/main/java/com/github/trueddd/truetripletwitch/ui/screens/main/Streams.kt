package com.github.trueddd.truetripletwitch.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.github.trueddd.twitch.data.Stream

@Composable
fun StreamerName(name: String) {
    Text(
        text = name,
        fontSize = 16.sp,
        maxLines = 1,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        fontWeight = FontWeight.Bold,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun StreamTitle(title: String) {
    Text(
        text = title,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        overflow = TextOverflow.Ellipsis,
        maxLines = 1,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun StreamInfo(value: String) {
    Text(
        text = value,
        fontSize = 14.sp,
        color = MaterialTheme.colorScheme.secondary,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Preview(
    widthDp = 450,
    heightDp = 100,
)
@Composable
fun Stream(
    @PreviewParameter(StreamParameters::class)
    stream: Stream,
    onStreamClicked: (Stream) -> Unit = {},
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.inversePrimary)
            .clickable { onStreamClicked(stream) }
    ) {
        Box(
            modifier = Modifier
                .weight(2f)
                .aspectRatio(16/9f)
                .background(MaterialTheme.colorScheme.inversePrimary)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        stream.thumbnailUrl
                            .replace("{width}", "160")
                            .replace("{height}", "90")
                    )
                    .crossfade(true)
                    .build(),
                contentDescription = "${stream.userName} stream thumbnail",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer)
            )
            Box(modifier = Modifier
                .padding(4.dp)
                .background(MaterialTheme.colorScheme.secondary, RoundedCornerShape(4.dp))
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
                            .background(Color.Red, CircleShape)
                    )
                    Text(
                        text = stream.shortenedViewerCount,
                        color = MaterialTheme.colorScheme.onSecondary,
                        fontSize = 12.sp,
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .weight(3f)
        ) {
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(4.dp)
            ) {
                StreamerName(stream.userName)
                StreamTitle(stream.title)
                StreamInfo(stream.gameName)
            }
        }
    }
}

@Composable
fun Streams(
    streams: List<Stream>,
    onStreamClicked: (Stream) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
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
