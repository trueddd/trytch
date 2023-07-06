package com.github.trueddd.twitch.dto.twitch

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TwitchEmoteType {
    @SerialName("bitstier")
    BitsTier,
    @SerialName("follower")
    Follower,
    @SerialName("subscriptions")
    Subscriptions,
    @SerialName("globals")
    Globals,
    @SerialName("limitedtime")
    LimitedTime,
    @SerialName("smilies")
    Smilies,
    @SerialName("twofactor")
    TwoFactor,
}
