package com.github.trueddd.twitch.data

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "emote_info",
    primaryKeys = ["id", "provider"],
    indices = [
        Index("name"),
    ],
)
data class EmoteInfo(
    val id: String,
    val name: String,
    val provider: Emote.Provider,
)
