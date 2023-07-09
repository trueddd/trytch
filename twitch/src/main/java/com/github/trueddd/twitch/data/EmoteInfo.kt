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
    val global: Boolean,
) {

    val sortingOrder: Int
        get() = if (global) 1 else 0
}
