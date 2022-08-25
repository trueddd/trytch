package com.github.trueddd.twitch.dto

data class PaginatedTwitchResponse<T>(
    val data: T,
    val pagination: Pagination,
)
