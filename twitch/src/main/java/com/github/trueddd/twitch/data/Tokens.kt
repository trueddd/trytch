package com.github.trueddd.twitch.data

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "tokens",
    primaryKeys = ["userId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["id"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
            onUpdate = ForeignKey.CASCADE,
        )
    ],
)
data class Tokens(
    val userId: String,
    val accessToken: String,
    val expirationTime: Long,
)
