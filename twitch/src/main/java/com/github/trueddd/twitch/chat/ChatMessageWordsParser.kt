package com.github.trueddd.twitch.chat

import android.util.Patterns
import com.github.trueddd.twitch.data.MessageWord
import com.github.trueddd.twitch.emotes.EmotesProvider
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.withIndex

class ChatMessageWordsParser(
    private val emotesProvider: EmotesProvider,
) {

    private val wordSplitter = Regex("\\s+")

    private fun String.indexAfterRepeatedWhiteSpace(repeat: Int): Int {
        var startIndex = 0
        for (i in 0 until repeat) {
            startIndex = this.indexOf(' ', startIndex) + 1
        }
        return startIndex.coerceIn(indices)
    }

    suspend fun split(
        messageContent: String,
        twitchEmotesInfo: TwitchEmotesInfo,
    ): List<MessageWord> {
        val emotes = (twitchEmotesInfo as? TwitchEmotesInfo.Included)?.emotes ?: listOf()
        return messageContent.split(wordSplitter)
            .asFlow()
            .withIndex()
            .map { (index, word) ->
                val emote = emotesProvider.getEmote(word)
                val twitchEmote = emotes.firstOrNull { it.position.first == messageContent.indexAfterRepeatedWhiteSpace(index) }
                when {
                    word.startsWith("@") -> MessageWord.Mention(word)
                    Patterns.WEB_URL.matcher(word).matches() -> MessageWord.Link(word)
                    emote != null -> MessageWord.Emote(emote, word)
                    twitchEmote != null -> MessageWord.UnknownTwitchEmote(twitchEmote.id, word)
                    else -> MessageWord.Default(word)
                }
            }
            .toList()
    }
}
