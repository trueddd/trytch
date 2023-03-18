package com.github.trueddd.twitch.data

data class ChatMessage(
    val author: String,
    val content: String,
    val userColor: String? = null,
    val badges: List<String> = emptyList(),
) {

    companion object {
        private val wordSplitter = Regex("\\s+")
    }

    val words = content.split(wordSplitter)
        .map { word ->
            when {
                word.startsWith("@") -> MessageWord.Mention(word)
                else -> MessageWord.Default(word)
            }
        }
}
