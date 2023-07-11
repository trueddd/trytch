package com.github.trueddd.twitch.data

data class Emote(
    val id: String,
    val name: String,
    val provider: Provider,
    val global: Boolean,
    val versions: List<Version>,
) {

    companion object {

        fun test() = Emote(
            id = "12345",
            name = "peepoCute",
            provider = Provider.SevenTv,
            global = false,
            versions = listOf(
                Version(
                    width = 112,
                    height = 112,
                    url = "https://cdn.frankerfacez.com/emoticon/478103/4"
                )
            )
        )
    }

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
