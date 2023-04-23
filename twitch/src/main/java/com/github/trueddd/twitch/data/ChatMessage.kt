package com.github.trueddd.twitch.data

data class ChatMessage(
    val author: String,
    val userColor: String? = null,
    val badges: List<String> = emptyList(),
    val words: List<MessageWord>,
)
