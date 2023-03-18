package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.trueddd.truetripletwitch.ui.StatefulViewModel
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
    val player: ExoPlayer,
    private val chatManager: ChatManager,
) : StatefulViewModel<StreamScreenState>() {

    override fun initialState() = StreamScreenState.default(channel)

    init {
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
        chatManager.connectChat(channel)
            .onStart { Log.d(TAG, "Connecting chat") }
            .onEach { status -> updateState { it.copy(chatStatus = status) } }
            .onCompletion { Log.d(TAG, "Disconnecting chat") }
            .launchIn(viewModelScope)
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
