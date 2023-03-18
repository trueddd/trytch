package com.github.trueddd.twitch.dto

import com.google.gson.annotations.SerializedName

data class TwitchBadgeVersion(
    @SerializedName("id")
    val id: String,
    @SerializedName("image_url_1x")
    val imageUrl1x: String,
    @SerializedName("image_url_2x")
    val imageUrl2x: String,
    @SerializedName("image_url_4x")
    val imageUrl4x: String,
)
