package com.github.trueddd.twitch.dto.twitch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TwitchEmoteFormat(val formatValue: String) {
    @SerialName("static")
    Static("static"),
    @SerialName("animated")
    Animated("animated"),
}
