package com.github.trueddd.twitch.chat

import android.util.Patterns
import com.github.trueddd.twitch.data.MessageWord
import com.github.trueddd.twitch.emotes.EmotesProvider
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList

class ChatMessageWordsParser(
    private val emotesProvider: EmotesProvider,
) {

    private val wordSplitter = Regex("\\s+")

    suspend fun split(messageContent: String): List<MessageWord> {
        return messageContent.split(wordSplitter)
            .asFlow()
            .map { word ->
                val emote = emotesProvider.getEmote(word)
                when {
                    word.startsWith("@") -> MessageWord.Mention(word)
                    Patterns.WEB_URL.matcher(word).matches() -> MessageWord.Link(word)
                    emote != null -> MessageWord.Emote(emote, word)
                    else -> MessageWord.Default(word)
                }
            }
            .toList()
    }
}
