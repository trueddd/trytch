package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.trueddd.truetripletwitch.ui.StatefulViewModel
import com.github.trueddd.twitch.TwitchClient
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart

class StreamViewModel(
    private val streamId: String,
    private val twitchClient: TwitchClient,
    val player: ExoPlayer,
) : StatefulViewModel<StreamScreenState>() {

    override fun initialState() = StreamScreenState(streamId, null, emptyMap())

    init {
        twitchClient.getStreamVideoInfo(streamId)
            .onStart { Log.d("StreamVideoInfo", "Started") }
            .onEach { Log.d("StreamUrl", it.toString()) }
            .onEach { links ->
                updateState {
                    it.copy(
                        streamUri = Uri.parse(links.values.last()),
                        availableStreamLinks = links,
                    )
                }
            }
            .launchIn(viewModelScope)
        stateFlow
            .mapNotNull { it.streamUri }
            .onEach {
                val source = HlsMediaSource.Factory(DefaultHttpDataSource.Factory())
                    .createMediaSource(MediaItem.fromUri(it))
                player.setMediaSource(source)
                player.prepare()
            }
            .launchIn(viewModelScope)
    }

    override fun release() {
        Log.d("StreamViewModel", "clear")
        player.stop()
        player.clearMediaItems()
    }
}
