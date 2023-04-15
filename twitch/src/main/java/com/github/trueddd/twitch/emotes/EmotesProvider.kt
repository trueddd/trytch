package com.github.trueddd.twitch.emotes

import com.github.trueddd.twitch.data.Emote

interface EmotesProvider {

    /**
     * Update emote set, pass `null` to update global emotes
     */
    fun update(channel: String?)

    suspend fun getEmote(word: String): Emote?
}
