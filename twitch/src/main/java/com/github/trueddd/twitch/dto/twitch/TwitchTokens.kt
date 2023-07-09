package com.github.trueddd.twitch.dto.twitch

import com.github.trueddd.twitch.data.Tokens
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TwitchTokens(
    @SerialName("client_id")
    val clientId: String,
    @SerialName("user_id")
    val userId: String,
    val login: String,
    @SerialName("expires_in")
    val expiresIn: Long,
) {

    fun toTokens(accessToken: String) = Tokens(
        userId,
        accessToken,
        System.currentTimeMillis() + expiresIn * 1000,
    )
}
