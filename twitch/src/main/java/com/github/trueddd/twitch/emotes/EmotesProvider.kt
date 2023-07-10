package com.github.trueddd.twitch.emotes

import com.github.trueddd.twitch.data.Emote

interface EmotesProvider : EmoteStorage {

    companion object {
        val AllEmoteProviders = Emote.Provider.values().toList()
    }

    suspend fun getEmote(word: String, providers: List<Emote.Provider> = AllEmoteProviders): Emote?
}
