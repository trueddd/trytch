package com.github.trueddd.twitch.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "emote_versions",
    primaryKeys = ["id", "provider", "width", "height"],
    foreignKeys = [
        ForeignKey(
            entity = EmoteInfo::class,
            parentColumns = ["id", "provider"],
            childColumns = ["id", "provider"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
)
data class EmoteVersion(
    val id: String,
    val provider: Emote.Provider,
    val width: Int,
    val height: Int,
    val url: String,
)
