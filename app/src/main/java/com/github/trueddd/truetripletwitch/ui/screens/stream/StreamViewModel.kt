package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.trueddd.truetripletwitch.ui.StatefulViewModel
import com.github.trueddd.twitch.TwitchBadgesManager
import com.github.trueddd.twitch.TwitchStreamsManager
import com.github.trueddd.twitch.chat.ChatManager
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
) : StatefulViewModel<StreamScreenState>() {

    override fun initialState() = StreamScreenState.default(channel)

    fun initStreamScreen() {
        loadStreamVideoInfo()
        setupStreamPlayer()
        setupChat()
        loadChannelBadges()
    }

    private fun setupStreamPlayer() {
        stateFlow
            .mapNotNull { it.streamUri }
            .distinctUntilChanged()
            .onEach {
                val source = HlsMediaSource.Factory(DefaultHttpDataSource.Factory())
                    .createMediaSource(MediaItem.fromUri(it))
                player.setMediaSource(source)
                player.prepare()
            }
            .launchIn(viewModelScope)
    }

    private fun loadStreamVideoInfo() {
        twitchStreamsManager.getStreamVideoInfo(channel)
            .onEach { Log.d(TAG, "Stream links: $it") }
            .onEach { links ->
                updateState { state ->
                    state.copy(
                        streamUri = links.values.lastOrNull()?.let { Uri.parse(it) },
                        availableStreamLinks = links,
                    )
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
