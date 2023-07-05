package com.github.trueddd.twitch.dto.ffz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FfzGlobalEmotesResponse(
    @SerialName("default_sets")
    val defaultSets: List<Int>,
    val sets: Map<String, FfzEmoteSet>,
)
