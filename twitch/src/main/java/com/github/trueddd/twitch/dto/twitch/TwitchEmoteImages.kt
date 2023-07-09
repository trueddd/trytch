package com.github.trueddd.twitch.dto.twitch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TwitchEmoteImages(
    @SerialName("url_1x")
    val url1x: String?,
    @SerialName("url_2x")
    val url2x: String?,
    @SerialName("url_4x")
    val url4x: String?,
)
