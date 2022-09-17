package com.github.trueddd.twitch.chat

import com.github.trueddd.twitch.data.ChatStatus
import com.github.trueddd.twitch.db.TwitchDatabase
import kotlinx.coroutines.flow.Flow

interface ChatManager {

    companion object {
        fun create(
            database: TwitchDatabase,
        ): ChatManager {
            return ChatManagerImpl(database.twitchDao())
        }
    }

    fun connectChat(channel: String): Flow<ChatStatus>
}
