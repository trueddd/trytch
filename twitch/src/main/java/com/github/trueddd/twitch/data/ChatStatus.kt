package com.github.trueddd.twitch.data

sealed class ChatStatus {

    object Connecting : ChatStatus()

    data class Disconnected(val error: Exception?) : ChatStatus()

    data class Connected(
        val messages: List<ChatMessage>,
    ) : ChatStatus()
}
