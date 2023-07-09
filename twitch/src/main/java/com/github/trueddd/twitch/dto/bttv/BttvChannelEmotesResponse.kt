package com.github.trueddd.twitch.dto.bttv

import kotlinx.serialization.Serializable

@Serializable
data class BttvChannelEmotesResponse(
    val id: String,
    val avatar: String,
    val channelEmotes: List<BttvEmote>,
    val sharedEmotes: List<BttvEmote>,
)
