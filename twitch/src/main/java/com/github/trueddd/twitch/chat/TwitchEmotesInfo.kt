package com.github.trueddd.twitch.chat

import com.ktmi.tmi.messages.Emote

sealed class TwitchEmotesInfo {

    object NotIncluded : TwitchEmotesInfo()

    data class Included(val emotes: List<EmoteInfo>) : TwitchEmotesInfo() {

        companion object {
            fun from(emotes: List<Emote>) = Included(emotes.flatMap { emote -> emote.positions.map { EmoteInfo(emote.id, it.first .. it.second) } })
        }

        data class EmoteInfo(
            val id: String,
            val position: IntRange,
        )
    }
}
