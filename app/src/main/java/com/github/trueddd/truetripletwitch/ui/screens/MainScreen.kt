package com.github.trueddd.truetripletwitch.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.trueddd.truetripletwitch.ui.MainScreenState
import com.github.trueddd.twitch.data.Stream

@Composable
fun Toolbar(
    @PreviewParameter(MainScreenStateParameters::class)
    state: MainScreenState,
    onLoginButtonClicked: () -> Unit = {},
    onLogoutButtonClicked: () -> Unit = {},
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(MaterialTheme.colorScheme.onPrimary)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End,
            modifier = Modifier
                .fillMaxSize()
        ) {
            if (state.user == null) {
                Button(
                    onClick = onLoginButtonClicked,
                    enabled = !state.userLoading,
                    modifier = Modifier
                ) {
                    Text(
                        text = "Login"
                    )
                }
            } else {
                Text(text = state.user.displayName)
                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onLogoutButtonClicked,
                    enabled = !state.userLoading,
                    modifier = Modifier
                ) {
                    Text(
                        text = "Logout",
                        fontSize = 16.sp,
                    )
                }
            }
        }
        if (state.userLoading) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(4.dp)
                    .align(Alignment.BottomCenter)
            )
        }
    }
}

@Preview(
    widthDp = 720,
    heightDp = 260,
)
@Composable
fun Stream(@PreviewParameter(StreamParameters::class) stream: Stream) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.secondaryContainer)
            .clip(RoundedCornerShape(8.dp))
            .fillMaxWidth()
    ) {
//        AsyncImage(
//            model = stream.thumbnailUrl,
//            contentDescription = "${stream.userName} stream thumbnail",
//            contentScale = ContentScale.Crop,
//            modifier = Modifier
//                .fillMaxHeight()
//                .background(MaterialTheme.colorScheme.primaryContainer)
//                .aspectRatio(16f/9)
//        )
        Column() {
            Text(
                text = stream.title,
                fontSize = 14.sp
            )
            Text(
                text = stream.userName,
                fontSize = 14.sp
            )
        }
    }
}

// todo
@Composable
fun Streams(
    streams: List<Stream>,
) {
    LazyColumn(
    ) {
        items(streams) { stream ->
            Stream(stream = stream)
        }
    }
}

@Preview
@Composable
fun MainScreen(
    @PreviewParameter(MainScreenStateParameters::class)
    state: MainScreenState,
    onLoginButtonClicked: () -> Unit = {},
    onLogoutButtonClicked: () -> Unit = {},
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Toolbar(state, onLoginButtonClicked, onLogoutButtonClicked)
        if (state.user != null) {
            Streams(streams = state.streams)
        }
    }
}

class MainScreenStateParameters : PreviewParameterProvider<MainScreenState> {
    override val values: Sequence<MainScreenState>
        get() = sequenceOf(MainScreenState.test())
}

class StreamParameters : PreviewParameterProvider<Stream> {
    override val values: Sequence<Stream>
        get() = sequenceOf(Stream.test())
}
