package com.github.trueddd.twitch.data

import androidx.room.Entity

@Entity
data class Emote(
    val name: String,
    val provider: Provider,
    val versions: List<Version>,
) {

    data class Version(
        val width: Int,
        val height: Int,
        val url: String,
    )

    enum class Provider(val value: String) {
        Twitch("twitch"),
        SevenTv("7tv"),
        FrankerFacez("ffz"),
        BetterTtv("bttv"),
    }
}
