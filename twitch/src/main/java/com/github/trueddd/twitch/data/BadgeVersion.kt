package com.github.trueddd.twitch.data

import androidx.room.Entity

@Entity(
    tableName = "badge_versions",
    primaryKeys = ["badgeId", "id"],
)
data class BadgeVersion(
    val badgeId: String,
    val id: String,
    val imageUrl1x: String,
    val imageUrl2x: String,
    val imageUrl4x: String,
)
