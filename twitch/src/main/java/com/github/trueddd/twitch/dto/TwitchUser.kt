package com.github.trueddd.twitch.dto

import com.github.trueddd.twitch.data.User
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TwitchUser(
    val id: String,
    val login: String,
    @SerialName("display_name")
    val displayName: String,
    val type: String,
    @SerialName("broadcaster_type")
    val broadcasterType: String,
    val description: String,
    @SerialName("profile_image_url")
    val profileImageUrl: String,
    val email: String,
) {

    fun toUser() = User(
        id,
        login,
        displayName,
        email,
        profileImageUrl,
    )
}
