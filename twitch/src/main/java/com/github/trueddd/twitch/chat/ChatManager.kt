package com.github.trueddd.twitch.chat

import com.github.trueddd.twitch.TwitchBadgesManager
import com.github.trueddd.twitch.data.ChatMessage
import com.github.trueddd.twitch.data.ConnectionStatus
import com.github.trueddd.twitch.db.TwitchDatabase
import com.github.trueddd.twitch.emotes.EmotesProvider
import kotlinx.coroutines.flow.Flow

interface ChatManager {

    companion object {
        fun create(
            twitchBadgesManager: TwitchBadgesManager,
            emotesProvider: EmotesProvider,
            database: TwitchDatabase,
        ): ChatManager {
            return ChatManagerImpl(twitchBadgesManager, database.twitchDao(), emotesProvider)
        }
    }

    fun initialize()

    fun connectChat(channel: String): Flow<ConnectionStatus>

    fun getMessagesFlow(channel: String): Flow<ChatMessage>

    suspend fun sendMessage(channel: String, text: String)
}
