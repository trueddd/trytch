package com.github.trueddd.twitch.chat

import android.util.Patterns
import com.github.trueddd.twitch.data.MessageWord

object ChatMessageWordsParser {

    private val wordSplitter = Regex("\\s+")

    fun split(messageContent: String): List<MessageWord> {
        return messageContent.split(wordSplitter)
            .map { word ->
                when {
                    word.startsWith("@") -> MessageWord.Mention(word)
                    Patterns.WEB_URL.matcher(word).matches() -> MessageWord.Link(word)
                    else -> MessageWord.Default(word)
                }
            }
    }
}
