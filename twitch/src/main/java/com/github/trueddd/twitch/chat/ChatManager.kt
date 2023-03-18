package com.github.trueddd.twitch.chat

import com.github.trueddd.twitch.TwitchBadgesManager
import com.github.trueddd.twitch.data.ChatStatus
import com.github.trueddd.twitch.db.TwitchDatabase
import kotlinx.coroutines.flow.Flow

interface ChatManager {

    companion object {
        fun create(
            twitchBadgesManager: TwitchBadgesManager,
            database: TwitchDatabase,
        ): ChatManager {
            return ChatManagerImpl(twitchBadgesManager, database.twitchDao())
        }
    }

    fun connectChat(channel: String): Flow<ChatStatus>
}
