package com.github.trueddd.twitch.data

sealed class MessageWord(open val content: String) {
    data class Default(override val content: String) : MessageWord(content)
    data class Mention(override val content: String) : MessageWord(content)
    data class Link(override val content: String) : MessageWord(content)
    data class Emote(
        val emote: com.github.trueddd.twitch.data.Emote,
        override val content: String,
    ) : MessageWord(content)
}
