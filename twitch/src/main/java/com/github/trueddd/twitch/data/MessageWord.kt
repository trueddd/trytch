package com.github.trueddd.twitch.data

sealed class MessageWord(val content: String) {
    class Default(content: String) : MessageWord(content)
    class Mention(content: String) : MessageWord(content)
}
