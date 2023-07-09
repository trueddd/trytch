package com.github.trueddd.twitch.dto.twitch

import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    val cursor: String?,
)
