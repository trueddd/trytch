package com.github.trueddd.twitch.dto.ffz

import kotlinx.serialization.Serializable

@Serializable
data class FfzChannelEmotesResponse(
    val room: FfzRoom,
    val sets: Map<String, FfzEmoteSet>,
)
