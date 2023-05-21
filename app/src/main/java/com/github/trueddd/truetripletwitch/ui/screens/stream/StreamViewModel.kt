package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.trueddd.truetripletwitch.player.playbackStateFlow
import com.github.trueddd.truetripletwitch.ui.StatefulViewModel
import com.github.trueddd.twitch.TwitchBadgesManager
import com.github.trueddd.twitch.TwitchStreamsManager
import com.github.trueddd.twitch.chat.ChatManager
import com.github.trueddd.twitch.emotes.EmoteUpdateOption
import com.github.trueddd.twitch.emotes.EmotesProvider
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import kotlinx.coroutines.flow.*

class StreamViewModel(
    private val channel: String,
    private val twitchStreamsManager: TwitchStreamsManager,
    private val twitchBadgesManager: TwitchBadgesManager,
    val player: ExoPlayer,
    private val chatManager: ChatManager,
    private val emotesProvider: EmotesProvider,
) : StatefulViewModel<StreamScreenState>() {

    init {
        initStreamScreen()
    }

    override fun initialState() = StreamScreenState.default(channel)

    private fun initStreamScreen() {
        loadStreamVideoInfo()
        setupStreamPlayer()
        setupChat()
        loadChannelBadges()
        updateEmotes()
        loadStreamInfo()
    }

    private fun loadStreamInfo() {
        twitchStreamsManager.getStreamFlow(channel)
            .onEach { stream -> updateState { it.copy(stream = stream) } }
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
                updateState { state ->
                    val stream = streamInfoList.lastOrNull()
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
