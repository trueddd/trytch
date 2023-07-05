package com.github.trueddd.twitch.emotes

sealed class EmoteUpdateOption {
    data class Channel(val name: String) : EmoteUpdateOption() {
        override fun toString() = "EmoteUpdateOption.Channel($name)"
    }
    object Global : EmoteUpdateOption() {
        override fun toString() = "EmoteUpdateOption.Global"
    }
}
