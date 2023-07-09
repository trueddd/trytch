package com.github.trueddd.twitch.dto.twitch

import kotlinx.serialization.Serializable

@Serializable
data class TwitchEmotesResponse<T: TwitchEmote>(
    val data: List<T>,
    val template: String,
) {

    fun toEmotes() = data.map { it.toEmote(template) }
}
