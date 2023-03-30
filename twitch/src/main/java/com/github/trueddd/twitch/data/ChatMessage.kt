package com.github.trueddd.twitch.data

import com.github.trueddd.twitch.chat.ChatMessageWordsParser

data class ChatMessage(
    val author: String,
    val content: String,
    val userColor: String? = null,
    val badges: List<String> = emptyList(),
) {

    val words = ChatMessageWordsParser.split(content)
}
