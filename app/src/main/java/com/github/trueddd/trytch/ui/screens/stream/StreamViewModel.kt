package com.github.trueddd.trytch.ui.screens.stream

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.trueddd.trytch.player.playbackStateFlow
import com.github.trueddd.trytch.settings.SettingsManager
import com.github.trueddd.trytch.settings.modifyStreamSettings
import com.github.trueddd.trytch.ui.StatefulViewModel
import com.github.trueddd.twitch.TwitchBadgesManager
import com.github.trueddd.twitch.TwitchStreamsManager
import com.github.trueddd.twitch.chat.ChatManager
import com.github.trueddd.twitch.emotes.EmoteUpdateOption
import com.github.trueddd.twitch.emotes.EmotesProvider
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class StreamViewModel(
    private val channel: String,
    private val twitchStreamsManager: TwitchStreamsManager,
    private val twitchBadgesManager: TwitchBadgesManager,
    val player: ExoPlayer,
    private val chatManager: ChatManager,
    private val emotesProvider: EmotesProvider,
    private val settingsManager: SettingsManager,
) : StatefulViewModel<StreamScreenState>() {

    init {
        initStreamScreen()
    }

    override fun initialState() = StreamScreenState.default(
        channel = channel,
        streamSettings = settingsManager.settingsFlow.value.streamSettings,
    )

    private fun initStreamScreen() {
        loadStreamVideoInfo()
        setupStreamPlayer()
        setupChat()
        loadChannelBadges()
        updateEmotes()
        loadStreamInfo()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun loadStreamInfo() {
        twitchStreamsManager.getStreamFlow(channel)
            .onEach { stream -> updateState { it.copy(stream = stream) } }
            .filterNotNull()
            .flatMapLatest { twitchStreamsManager.getStreamBroadcasterUserFlow(it.userId) }
            .onEach { user -> updateState { it.copy(broadcaster = user) } }
            .launchIn(viewModelScope)
    }

    private fun setupStreamPlayer() {
        stateFlow
            .mapNotNull { it.playerStatus.streamUri }
            .distinctUntilChanged()
            .onEach {
                val source = HlsMediaSource.Factory(DefaultHttpDataSource.Factory())
                    .createMediaSource(MediaItem.fromUri(it))
                player.setMediaSource(source)
                player.playWhenReady = true
                player.prepare()
            }
            .launchIn(viewModelScope)
        player.playbackStateFlow()
            .onEach { (isBuffering, isPlaying) ->
                updateState {
                    val playerStatus = it.playerStatus.copy(
                        isPlaying = isPlaying,
                        isBuffering = isBuffering,
                    )
                    it.copy(playerStatus = playerStatus)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun loadStreamVideoInfo() {
        twitchStreamsManager.getStreamVideoInfo(channel)
            .onEach { Log.d(TAG, "Stream links: $it") }
            .onEach { streamInfoList ->
                val streamSettings = settingsManager.settingsFlow.value.streamSettings
                updateState { state ->
                    val stream = streamInfoList.firstOrNull { it.quality == streamSettings.preferredQuality }
                        ?: streamInfoList.lastOrNull()
                    val playerStatus = state.playerStatus.copy(
                        streamUri = stream?.url?.let { Uri.parse(it) },
                        streamLinks = streamInfoList,
                        selectedStream = stream?.quality,
                    )
                    state.copy(playerStatus = playerStatus)
                }
            }
            .launchIn(viewModelScope)
    }

    private fun setupChat() {
        chatManager.connectChat(channel)
            .onStart { Log.d(TAG, "Connecting chat") }
            .onEach { status -> updateState { it.copy(chatStatus = status) } }
            .onCompletion { Log.d(TAG, "Disconnecting chat") }
            .launchIn(viewModelScope)
    }

    private fun loadChannelBadges() {
        twitchBadgesManager.updateChannelBadges(channel)
    }

    private fun updateEmotes() {
        emotesProvider.update(EmoteUpdateOption.Channel(channel))
    }

    fun updateChatOverlayVisibility(visible: Boolean) {
        updateState { state ->
            state.copy(chatOverlayStatus = state.chatOverlayStatus.copy(enabled = visible))
        }
        settingsManager.modifyStreamSettings { it.copy(chatOverlayEnabled = visible) }
    }

    fun updateChatOverlayOpacity(opacity: Float) {
        updateState { state ->
            state.copy(chatOverlayStatus = state.chatOverlayStatus.copy(opacity = opacity))
        }
        settingsManager.modifyStreamSettings { it.copy(chatOverlayOpacity = opacity) }
    }

    fun updateChatOverlaySize(size: ChatOverlayStatus.Size) {
        updateState { state ->
            state.copy(chatOverlayStatus = state.chatOverlayStatus.copy(size = size))
        }
        settingsManager.modifyStreamSettings {
            it.copy(chatOverlayHeightDp = size.heightDp, chatOverlayWidthDp = size.widthDp)
        }
    }

    fun saveChatOverlayPosition(shiftX: Float, shiftY: Float) {
        settingsManager.modifyStreamSettings { it.copy(chatOverlayShiftX = shiftX, chatOverlayShiftY = shiftY) }
    }

    fun handlePlayerEvent(playerEvent: PlayerEvent) {
        when (playerEvent) {
            is PlayerEvent.AspectRatioChange -> {
                updateState { streamScreenState ->
                    val playerStatus = streamScreenState.playerStatus.copy(aspectRatio = playerEvent.newValue)
                    streamScreenState.copy(playerStatus = playerStatus)
                }
            }
            is PlayerEvent.StreamQualityChange -> {
                updateState { streamScreenState ->
                    val playerStatus = streamScreenState.playerStatus.copy(
                        selectedStream = playerEvent.newValue,
                        streamUri = streamScreenState.playerStatus
                            .streamLinks.firstOrNull { it.quality == playerEvent.newValue }
                            ?.let { Uri.parse(it.url) },
                    )
                    streamScreenState.copy(playerStatus = playerStatus)
                }
                settingsManager.modifyStreamSettings { it.copy(preferredQuality = playerEvent.newValue) }
            }
        }
    }

    override fun release() {
        super.release()
        Log.d(TAG, "clear")
        player.stop()
        player.clearMediaItems()
    }

    companion object {
        const val TAG = "StreamViewModel"
    }
}
