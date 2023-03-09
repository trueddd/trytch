package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.github.trueddd.truetripletwitch.ui.modifyIf
import com.github.trueddd.twitch.data.ChatMessage
import com.github.trueddd.twitch.data.ChatStatus
import com.google.accompanist.flowlayout.FlowRow
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.StyledPlayerView

class StreamScreen(
    private val streamViewModel: StreamViewModel,
    buildContext: BuildContext,
) : Node(buildContext) {

    @Composable
    override fun View(modifier: Modifier) {
        val state by streamViewModel.stateFlow.collectAsState()
        StreamScreen(state, streamViewModel.player)
    }
}

@Composable
fun Player(player: ExoPlayer) {
    AndroidView(
        factory = {
            StyledPlayerView(it).apply {
                hideController()
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                this.player = player
            }
        },
        modifier = Modifier
            .fillMaxSize(),
    )
}

@Composable
fun StreamScreen(
    @PreviewParameter(StreamStateParameterProvider::class)
    state: StreamScreenState,
    player: ExoPlayer,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.inversePrimary)
    ) {
        Box(
            modifier = Modifier
                .modifyIf(LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    this
                        .fillMaxWidth()
                        .fillMaxHeight(0.3f)
                }
                .modifyIf(LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    this.fillMaxSize()
                }
                .background(MaterialTheme.colorScheme.error)
        ) {
            if (state.streamUri != null) {
                Player(player)
            }
        }
        if (LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT) {
            Chat(state.chatStatus)
        }
    }
}

@Preview
@Composable
fun Chat(
    @PreviewParameter(provider = ChatStatusParameterProvider::class)
    chatStatus: ChatStatus,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surfaceTint)
    ) {
        if (chatStatus is ChatStatus.Connecting) {
            CircularProgressIndicator(
                modifier = Modifier
                    .align(Alignment.Center)
                    .size(48.dp)
            )
        }
        if (chatStatus is ChatStatus.Disconnected) {
            Text(
                text = chatStatus.error?.message ?: "QWE",
                modifier = Modifier
                    .align(Alignment.Center)
            )
        }
        if (chatStatus is ChatStatus.Connected) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                items(chatStatus.messages) {
                    FlowRow(mainAxisSpacing = 2.dp) {
                        Text(text = it.author)
                        // fixme: remove split logic from UI
                        it.content.split(Regex("\\s+")).forEach { messageWord ->
                            Text(text = messageWord)
                        }
                    }
                }
            }
        }
    }
}

class StreamStateParameterProvider : PreviewParameterProvider<StreamScreenState> {
    override val values = sequenceOf(StreamScreenState.test())
}

class ChatStatusParameterProvider : PreviewParameterProvider<ChatStatus> {
    override val values = sequenceOf(
        ChatStatus.Connected(listOf(
            ChatMessage("truetripled", "hello")
        )),
        ChatStatus.Disconnected(null),
        ChatStatus.Connecting,
    )
}
