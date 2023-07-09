package com.github.trueddd.twitch.dto.ffz

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FfzRoom(
    val id: String,
    @SerialName("display_name")
    val displayName: String,
    @SerialName("set")
    val setId: Int,
)
