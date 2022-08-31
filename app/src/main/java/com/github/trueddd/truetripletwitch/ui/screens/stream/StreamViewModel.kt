package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.github.trueddd.truetripletwitch.ui.StatefulViewModel
import com.github.trueddd.twitch.TwitchClient
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class StreamViewModel(
    private val streamId: String,
    private val twitchClient: TwitchClient,
) : StatefulViewModel<StreamScreenState>() {

    override fun initialState() = StreamScreenState(streamId, null)

    init {
        twitchClient.getStreamVideoInfo(streamId)
            .onEach { Log.d("StreamUrl", it.toString()) }
            .onEach { links -> updateState { it.copy(streamUri = Uri.parse(links.values.last())) } }
            .launchIn(viewModelScope)
    }
}
