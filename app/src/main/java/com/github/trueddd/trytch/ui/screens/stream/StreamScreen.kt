package com.github.trueddd.trytch.ui.screens.stream

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.bumble.appyx.core.lifecycle.asFlow
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.github.trueddd.trytch.ui.isLandscape
import com.github.trueddd.trytch.ui.isPortrait
import com.github.trueddd.trytch.ui.modifyIf
import com.github.trueddd.trytch.ui.theme.AppTheme
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StreamScreen(
    private val streamViewModel: StreamViewModel,
    buildContext: BuildContext,
) : Node(buildContext) {

    @Composable
    override fun View(modifier: Modifier) {
        LaunchedEffect(Unit) {
            lifecycle.asFlow()
                .filter { it != Lifecycle.State.RESUMED }
                .distinctUntilChanged()
                .onEach { streamViewModel.player.pause() }
                .launchIn(this)
        }
        val state by streamViewModel.stateFlow.collectAsState()
        StreamScreen(
            state,
            streamViewModel.player,
            Modifier.fillMaxSize(),
            playerEvents = { streamViewModel.handlePlayerEvent(it) },
            chatOverlayChecked = { streamViewModel.updateChatOverlayVisibility(it) },
            chatOverlayOpacityChanged = { streamViewModel.updateChatOverlayOpacity(it) },
            onChatOverlayDragged = { streamViewModel.saveChatOverlayPosition(it.x, it.y) },
            chatOverlaySizeChanged = { streamViewModel.updateChatOverlaySize(it) },
            onSendMessageClicked = { streamViewModel.sendMessage(it) },
        )
    }
}

@Preview
@Composable
private fun StreamScreenPreview() {
    StreamScreen(
        state = StreamScreenState.test(),
        player = null,
    )
}

@Composable
fun StreamScreen(
    @PreviewParameter(provider = StreamScreenStateProvider::class)
    state: StreamScreenState,
    player: ExoPlayer?,
    modifier: Modifier = Modifier,
    onSendMessageClicked: (String) -> Unit = {},
    playerEvents: (PlayerEvent) -> Unit = {},
    chatOverlayChecked: (Boolean) -> Unit = {},
    chatOverlayOpacityChanged: (Float) -> Unit = {},
    chatOverlaySizeChanged: (ChatOverlayStatus.Size) -> Unit = {},
    onChatOverlayDragged: (Offset) -> Unit = {},
) {
    Column(
        modifier = modifier
            .background(AppTheme.Primary)
    ) {
        val configuration = LocalConfiguration.current
        Box(
            modifier = Modifier
                .modifyIf(configuration.isPortrait) {
                    this
                        .fillMaxWidth()
                        .aspectRatio(16f / 9)
                }
                .modifyIf(configuration.isLandscape) {
                    this.fillMaxSize()
                }
                .background(AppTheme.Secondary)
        ) {
            PlayerContainer(
                player = player,
                stream = state.stream,
                broadcaster = state.broadcaster,
                playerStatus = state.playerStatus,
                chatStatus = state.chatStatus,
                playerEvents = playerEvents,
                chatOverlayStatus = state.chatOverlayStatus,
                chatOverlayChecked = chatOverlayChecked,
                chatOverlayOpacityChanged = chatOverlayOpacityChanged,
                chatOverlaySizeChanged = chatOverlaySizeChanged,
                onChatOverlayDragged = onChatOverlayDragged,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        if (configuration.isPortrait) {
            var emotesPanelOpen by remember { mutableStateOf(false) }
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) chat@{
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Chat(
                        chatStatus = state.chatStatus,
                        modifier = Modifier
                            .fillMaxSize()
                    )
                    this@chat.AnimatedVisibility(
                        visible = emotesPanelOpen,
                        enter = slideInVertically { it },
                        exit = slideOutVertically { it },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        EmotesPanel()
                    }
                }
                ChatInput(
                    emotesOpen = emotesPanelOpen,
                    onSendMessageClicked = onSendMessageClicked,
                    onEmoteButtonClicked = { emotesPanelOpen = !emotesPanelOpen },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        }
    }
}

class StreamScreenStateProvider : PreviewParameterProvider<StreamScreenState> {
    override val values = sequenceOf(StreamScreenState.test())
}
