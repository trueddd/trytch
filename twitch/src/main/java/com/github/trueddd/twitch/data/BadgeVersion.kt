package com.github.trueddd.twitch.data

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(
    tableName = "badge_versions",
    primaryKeys = ["badgeId", "id", "channel"],
)
data class BadgeVersion(
    val badgeId: String,
    val id: String,
    @ColumnInfo(defaultValue = "")
    val channel: String,
    val imageUrl1x: String,
    val imageUrl2x: String,
    val imageUrl4x: String,
)
