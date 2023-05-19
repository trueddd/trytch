package com.github.trueddd.truetripletwitch.ui.screens.stream

import com.github.trueddd.twitch.data.ChatMessage
import com.github.trueddd.twitch.data.ChatStatus
import com.github.trueddd.twitch.data.ConnectionStatus
import com.github.trueddd.twitch.data.Stream
import com.github.trueddd.twitch.data.MessageWord
import kotlinx.collections.immutable.persistentListOf

data class StreamScreenState(
    val channel: String,
    val stream: Stream?,
    val chatStatus: ChatStatus,
    val playerStatus: PlayerStatus,
) {

    companion object {
        fun test() = StreamScreenState(
            channel = "qwe1",
            stream = Stream.test(),
            chatStatus = ChatStatus(
                messages = persistentListOf(
                    ChatMessage("qwe1", words = listOf(MessageWord.Default("Hello"))),
                    ChatMessage("qwe2", words = listOf(MessageWord.Default("Hi!"))),
                ),
                connectionStatus = ConnectionStatus.Connected,
            ),
            playerStatus = PlayerStatus.test(),
        )

        fun default(channel: String) = StreamScreenState(
            channel,
            stream = null,
            chatStatus = ChatStatus(
                messages = persistentListOf(),
                connectionStatus = ConnectionStatus.Disconnected(null),
            ),
            playerStatus = PlayerStatus.default(),
        )
    }
}
