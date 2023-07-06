package com.github.trueddd.twitch.dto.twitch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TwitchBadgeVersion(
    @SerialName("id")
    val id: String,
    @SerialName("image_url_1x")
    val imageUrl1x: String,
    @SerialName("image_url_2x")
    val imageUrl2x: String,
    @SerialName("image_url_4x")
    val imageUrl4x: String,
)
