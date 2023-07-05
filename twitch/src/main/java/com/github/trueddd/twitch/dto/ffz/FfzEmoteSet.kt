package com.github.trueddd.twitch.dto.ffz

import kotlinx.serialization.Serializable

@Serializable
data class FfzEmoteSet(
    val id: Int,
    val title: String,
    val emoticons: List<FfzEmoticon>,
)
