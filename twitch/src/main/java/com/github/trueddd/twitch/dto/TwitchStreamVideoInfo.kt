package com.github.trueddd.twitch.dto

import kotlinx.serialization.Serializable

@Serializable
data class TwitchStreamVideoInfo(
    val urls: Map<String, String>,
)
