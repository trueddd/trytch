package com.github.trueddd.trytch.ui.screens.stream

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.Lifecycle
import com.bumble.appyx.core.lifecycle.asFlow
import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node
import com.bumble.appyx.navmodel.backstack.operation.push
import com.github.trueddd.trytch.navigation.AppBackStack
import com.github.trueddd.trytch.navigation.Routing
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
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach

class StreamScreen(
    private val streamViewModel: StreamViewModel,
    private val emotesPanelViewModel: EmotesPanelViewModel,
    private val appBackStack: AppBackStack,
    buildContext: BuildContext,
) : Node(buildContext) {

    @Composable
    override fun View(modifier: Modifier) {
        LaunchedEffect(Unit) {
            lifecycle.asFlow()
                .map { it == Lifecycle.State.RESUMED }
                .distinctUntilChanged()
                .onEach { screenActive ->
                    if (screenActive)  {
                        streamViewModel.player.resume()
                    } else {
                        streamViewModel.player.pause()
                    }
                }
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
            onStreamerClicked = {
                val user = streamScreenState.broadcaster ?: return@StreamScreen
                appBackStack.push(Routing.StreamerPage(user))
            },
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
    onStreamerClicked: () -> Unit = {},
) {
    var chatInputText by remember { mutableStateOf("") }
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
                onStreamerClicked = onStreamerClicked,
                modifier = Modifier
                    .fillMaxSize()
            )
        }
        if (configuration.isPortrait) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Chat(
                    chatStatus = state.chatStatus,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                )
                if (emotesPanelState.panelOpen) {
                    EmotesPanel(
                        emotesPanelState = emotesPanelState,
                        onEmotesTabChanged = onEmotesPanelTabChanged,
                        onSearchToggled = onEmotesPanelSearchToggled,
                        onSearchTextChanged = onEmotesPanelSearchTextChanged,
                        onEmoteClicked = { chatInputText = chatInputText.appendChatEmote(it) },
                    )
                }
                ChatInput(
                    text = chatInputText,
                    emotesOpen = emotesPanelState.panelOpen,
                    onTextChanged = { chatInputText = it },
                    onSendMessageClicked = { onSendMessageClicked(chatInputText) },
                    onEmoteButtonClicked = onEmotesPanelToggle,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            }
        }
    }
}

private fun String.appendChatEmote(emote: Emote): String {
    return when {
        isEmpty() || endsWith(" ") -> "$this${emote.name}"
        else -> "$this ${emote.name}"
    }
}
