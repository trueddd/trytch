package com.github.trueddd.twitch.dto

import com.github.trueddd.twitch.data.Emote
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SevenTvEmote(
    val id: String,
    val name: String,
    val visibility: Int,
    @SerialName("visibility_simple")
    val visibilitySimple: List<String>,
    val mime: String,
    val status: Int,
    val width: List<Int>,
    val height: List<Int>,
    val urls: List<List<String>>,
) {

    fun toEmote() = Emote(
        id,
        name,
        Emote.Provider.SevenTv,
        List(urls.size) { Emote.Version(width[it], height[it], urls[it].last()) },
    )
}
