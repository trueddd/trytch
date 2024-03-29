package com.github.trueddd.twitch.data

data class ChatMessage(
    val channel: String,
    val author: String,
    val userColor: String? = null,
    val badges: List<String> = emptyList(),
    val words: List<MessageWord>,
) {

    companion object {
        fun test() = ChatMessage(
            author = "elptripledo",
            channel = "twitch",
            words = listOf(
                MessageWord.Default("Hello,"),
                MessageWord.Default("my"),
                MessageWord.Default("name"),
                MessageWord.Default("is"),
                MessageWord.Default("very"),
                MessageWord.Default("long!"),
            )
        )
    }
}
