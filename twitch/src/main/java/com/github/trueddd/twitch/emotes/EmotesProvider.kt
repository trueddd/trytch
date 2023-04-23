package com.github.trueddd.twitch.emotes

import com.github.trueddd.twitch.data.Emote

interface EmotesProvider : EmoteStorage {

    suspend fun getEmote(word: String): Emote?
}
