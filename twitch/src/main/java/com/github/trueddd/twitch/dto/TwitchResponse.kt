package com.github.trueddd.twitch.dto

import kotlinx.serialization.Serializable

@Serializable
data class TwitchResponse<T>(
    val data: T,
)
