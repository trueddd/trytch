package com.github.trueddd.twitch.data

interface ChatMessage {
    val author: String
    val content: String
}

fun ChatMessage(
    author: String,
    content: String,
) = object : ChatMessage {
    override val author = author
    override val content = content
}
