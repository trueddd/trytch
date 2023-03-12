package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.net.Uri
import com.github.trueddd.twitch.data.ChatMessage
import com.github.trueddd.twitch.data.ChatStatus
import com.github.trueddd.twitch.data.ConnectionStatus

data class StreamScreenState(
    val channel: String,
    val streamUri: Uri?,
    val availableStreamLinks: Map<String, String>,
    val chatStatus: ChatStatus,
) {

    companion object {
        fun test() = StreamScreenState(
            channel = "qwe1",
            streamUri = Uri.EMPTY,
            availableStreamLinks = mapOf(
                "360p" to "qwerty",
                "480p" to "qwerty",
                "720p" to "qwerty",
            ),
            chatStatus = ChatStatus(
                messages = listOf(
                    ChatMessage("qwe1", "Hello"),
                    ChatMessage("qwe2", "Hi!"),
                ),
                connectionStatus = ConnectionStatus.Connected,
            )
        )

        fun default(channel: String) = StreamScreenState(
            channel,
            streamUri = null,
            availableStreamLinks = emptyMap(),
            chatStatus = ChatStatus(
                messages = emptyList(),
                connectionStatus = ConnectionStatus.Disconnected(null),
            )
        )
    }
}
