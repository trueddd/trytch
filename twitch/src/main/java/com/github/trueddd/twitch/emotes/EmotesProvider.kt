package com.github.trueddd.twitch.emotes

import com.github.trueddd.twitch.data.Emote
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface EmotesProvider : EmoteStorage {

    companion object {
        val AllEmoteProviders = Emote.Provider.values().toList()
    }

    suspend fun getEmote(word: String, providers: List<Emote.Provider> = AllEmoteProviders): Emote?

    fun getEmotesFlow(provider: Emote.Provider): Flow<ImmutableList<Emote>>

    fun getEmotesByNameFlow(query: String): Flow<ImmutableList<Emote>>
}
