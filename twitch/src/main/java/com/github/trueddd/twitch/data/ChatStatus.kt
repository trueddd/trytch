package com.github.trueddd.twitch.data

data class ChatStatus(
    val messages: List<ChatMessage>,
    val connectionStatus: ConnectionStatus,
)
