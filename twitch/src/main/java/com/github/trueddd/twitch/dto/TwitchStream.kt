package com.github.trueddd.twitch.dto

import com.github.trueddd.twitch.data.Stream
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TwitchStream(
    val id: String,
    @SerialName("user_id")
    val userId: String,
    @SerialName("user_login")
    val userLogin: String,
    @SerialName("user_name")
    val userName: String,
    @SerialName("game_id")
    val gameId: String,
    @SerialName("game_name")
    val gameName: String,
    val type: String,
    val title: String,
    @SerialName("viewer_count")
    val viewerCount: Int,
    @SerialName("started_at")
    val startedAt: String,
    val language: String,
    @SerialName("thumbnail_url")
    val thumbnailUrl: String,
    val tags: List<String>?,
) {

    fun toStream(userId: String) = Stream(
        id,
        this.userId,
        userLogin,
        userName,
        gameId,
        gameName,
        type,
        title,
        viewerCount,
        startedAt,
        language,
        thumbnailUrl,
        tags ?: emptyList(),
        userId,
    )
}
