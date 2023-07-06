package com.github.trueddd.twitch.dto.twitch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TwitchEmoteFormat {
    @SerialName("static")
    Static,
    @SerialName("animated")
    Animated,
}
