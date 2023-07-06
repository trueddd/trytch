package com.github.trueddd.twitch.dto.twitch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TwitchEmoteThemeMode {
    @SerialName("dark")
    Dark,
    @SerialName("light")
    Light,
}
