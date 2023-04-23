package com.github.trueddd.twitch.emotes

sealed class EmoteUpdateOption {
    data class Channel(val name: String) : EmoteUpdateOption()
    object Global : EmoteUpdateOption()
}
