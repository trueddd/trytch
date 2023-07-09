package com.github.trueddd.twitch.dto.twitch

import kotlinx.serialization.Serializable

@Serializable
data class PaginatedTwitchResponse<T>(
    val data: T,
    val pagination: Pagination,
)
