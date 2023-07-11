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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.bumble.appyx.core.lifecycle.asFlow
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.github.trueddd.trytch.ui.isLandscape
import com.github.trueddd.trytch.ui.isPortrait
import com.github.trueddd.trytch.ui.modifyIf
import com.github.trueddd.trytch.ui.screens.stream.chat.Chat
import com.github.trueddd.trytch.ui.screens.stream.chat.ChatInput
import com.github.trueddd.trytch.ui.screens.stream.chat.ChatOverlayStatus
import com.github.trueddd.trytch.ui.screens.stream.chat.EmotesPanelState
import com.github.trueddd.trytch.ui.screens.stream.chat.EmotesPanelViewModel
import com.github.trueddd.trytch.ui.theme.AppTheme
import com.github.trueddd.twitch.data.Emote
import com.google.android.exoplayer2.ExoPlayer
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StreamScreen(
    private val streamViewModel: StreamViewModel,
    private val emotesPanelViewModel: EmotesPanelViewModel,
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
        val streamScreenState by streamViewModel.stateFlow.collectAsState()
        val emotesPanelState by emotesPanelViewModel.stateFlow.collectAsState()
        StreamScreen(
            state = streamScreenState,
            emotesPanelState = emotesPanelState,
            player = streamViewModel.player,
            modifier = Modifier.fillMaxSize(),
            playerEvents = { streamViewModel.handlePlayerEvent(it) },
            chatOverlayChecked = { streamViewModel.updateChatOverlayVisibility(it) },
            chatOverlayOpacityChanged = { streamViewModel.updateChatOverlayOpacity(it) },
            onChatOverlayDragged = { streamViewModel.saveChatOverlayPosition(it.x, it.y) },
            chatOverlaySizeChanged = { streamViewModel.updateChatOverlaySize(it) },
            onSendMessageClicked = { streamViewModel.sendMessage(it) },
            onEmotesPanelToggle = { emotesPanelViewModel.togglePanel() },
            onEmotesPanelTabChanged = { emotesPanelViewModel.changeTab(it) },
            onEmotesPanelSearchToggled = { emotesPanelViewModel.toggleSearch() },
            onEmotesPanelSearchTextChanged = { emotesPanelViewModel.updateSearch(it) },
        )
    }
}

@Preview
@Composable
private fun StreamScreenPreview() {
    StreamScreen(
        state = StreamScreenState.test(),
        emotesPanelState = EmotesPanelState.test(),
        player = null,
    )
}

@Composable
fun StreamScreen(
    state: StreamScreenState,
    player: ExoPlayer?,
    emotesPanelState: EmotesPanelState,
    modifier: Modifier = Modifier,
    onSendMessageClicked: (String) -> Unit = {},
    playerEvents: (PlayerEvent) -> Unit = {},
    chatOverlayChecked: (Boolean) -> Unit = {},
    chatOverlayOpacityChanged: (Float) -> Unit = {},
    chatOverlaySizeChanged: (ChatOverlayStatus.Size) -> Unit = {},
    onChatOverlayDragged: (Offset) -> Unit = {},
    onEmotesPanelToggle: () -> Unit = {},
    onEmotesPanelTabChanged: (Emote.Provider) -> Unit = {},
    onEmotesPanelSearchToggled: () -> Unit = {},
    onEmotesPanelSearchTextChanged: (String) -> Unit = {},
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
                        visible = emotesPanelState.panelOpen,
                        enter = slideInVertically { it },
                        exit = slideOutVertically { it },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp)
                    ) {
                        EmotesPanel(
                            emotesPanelState = emotesPanelState,
                            onEmotesTabChanged = onEmotesPanelTabChanged,
                            onSearchToggled = onEmotesPanelSearchToggled,
                            onSearchTextChanged = onEmotesPanelSearchTextChanged,
                        )
                    }
                }
                ChatInput(
                    emotesOpen = emotesPanelState.panelOpen,
                    onSendMessageClicked = onSendMessageClicked,
                    onEmoteButtonClicked = onEmotesPanelToggle,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        }
    }
}
