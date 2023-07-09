package com.github.trueddd.twitch.dto.twitch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TwitchEmoteThemeMode(val modeValue: String) {
    @SerialName("dark")
    Dark("dark"),
    @SerialName("light")
    Light("light"),
}
