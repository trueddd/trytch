package com.github.trueddd.twitch.dto

import com.github.trueddd.twitch.data.Tokens
import com.google.gson.annotations.SerializedName

data class TwitchTokens(
    @SerializedName("client_id")
    val clientId: String,
    @SerializedName("user_id")
    val userId: String,
    val login: String,
    @SerializedName("expires_in")
    val expiresIn: Long,
) {

    fun toTokens(accessToken: String) = Tokens(
        userId,
        accessToken,
        System.currentTimeMillis() + expiresIn * 1000,
    )
}
