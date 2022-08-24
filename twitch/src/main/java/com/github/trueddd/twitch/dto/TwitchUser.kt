package com.github.trueddd.twitch.dto

import com.github.trueddd.twitch.data.User
import com.google.gson.annotations.SerializedName

data class TwitchUser(
    val id: String,
    val login: String,
    @SerializedName("display_name")
    val displayName: String,
    val type: String,
    @SerializedName("broadcaster_type")
    val broadcasterType: String,
    val description: String,
    @SerializedName("profile_image_url")
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
