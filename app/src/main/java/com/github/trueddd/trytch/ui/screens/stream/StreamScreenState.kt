package com.github.trueddd.trytch.ui.screens.stream

import com.github.trueddd.trytch.settings.StreamSettings
import com.github.trueddd.trytch.ui.screens.stream.chat.ChatOverlayStatus
import com.github.trueddd.trytch.ui.screens.stream.chat.ChatStatus
import com.github.trueddd.trytch.ui.screens.stream.chat.overlaySize
import com.github.trueddd.twitch.data.ChatMessage
import com.github.trueddd.twitch.data.ConnectionStatus
import com.github.trueddd.twitch.data.Stream
import com.github.trueddd.twitch.data.MessageWord
import com.github.trueddd.twitch.data.User
import kotlinx.collections.immutable.persistentListOf

data class StreamScreenState(
    val channel: String,
    val broadcaster: User?,
    val stream: Stream?,
    val chatStatus: ChatStatus,
    val playerStatus: PlayerStatus,
    val chatOverlayStatus: ChatOverlayStatus,
) {

    companion object {
        fun test() = StreamScreenState(
            channel = "qwe1",
            stream = Stream.test(),
            broadcaster = User.test(),
            chatStatus = ChatStatus(
                messages = persistentListOf(
                    ChatMessage(channel = "twitch", author = "qwe1", words = listOf(MessageWord.Default("Hello"))),
                    ChatMessage(channel = "twitch", author = "qwe2", words = listOf(MessageWord.Default("Hi!"))),
                ),
                connectionStatus = ConnectionStatus.Connected,
            ),
            playerStatus = PlayerStatus.test(),
            chatOverlayStatus = ChatOverlayStatus.test(),
        )

        fun default(
            channel: String,
            streamSettings: StreamSettings,
        ) = StreamScreenState(
            channel,
            stream = null,
            broadcaster = null,
            chatStatus = ChatStatus(
                messages = persistentListOf(),
                connectionStatus = ConnectionStatus.Disconnected(null),
            ),
            playerStatus = PlayerStatus.default(),
            chatOverlayStatus = ChatOverlayStatus(
                enabled = streamSettings.chatOverlayEnabled,
                opacity = streamSettings.chatOverlayOpacity,
                shiftX = streamSettings.chatOverlayShiftX,
                shiftY = streamSettings.chatOverlayShiftY,
                size = streamSettings.overlaySize(),
            ),
        )
    }
}
