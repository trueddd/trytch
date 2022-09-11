package com.github.trueddd.truetripletwitch.ui.screens.stream

import android.net.Uri
import com.github.trueddd.twitch.data.ChatMessage
import com.github.trueddd.twitch.data.ChatStatus

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
            chatStatus = ChatStatus.Connected(
                messages = listOf(
                    ChatMessage("qwe1", "Hello"),
                    ChatMessage("qwe2", "Hi!"),
                )
            )
        )
    }
}
