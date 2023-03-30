package com.github.trueddd.twitch.dto

import kotlinx.serialization.Serializable

@Serializable
data class Pagination(
    val cursor: String?,
)
