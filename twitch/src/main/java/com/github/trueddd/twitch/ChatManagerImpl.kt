package com.github.trueddd.twitch

import android.util.Log
import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.trueddd.twitch.data.ChatMessage
import com.github.trueddd.twitch.data.ChatStatus
import com.github.trueddd.twitch.db.TwitchDao
import com.github.twitch4j.TwitchClientBuilder
import com.github.twitch4j.chat.events.AbstractChannelEvent
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.github.twitch4j.chat.events.channel.IRCMessageEvent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import java.util.*

internal class ChatManagerImpl(
    private val twitchDao: TwitchDao,
) : ChatManager {

    companion object {
        const val TAG = "ChatManager"
    }

    override fun connectChat(channel: String): Flow<ChatStatus> {
        return channelFlow<ChatStatus> {
            send(ChatStatus.Connecting)
            val userToken = twitchDao.getUserToken() ?: run {
                send(ChatStatus.Disconnected(IllegalStateException("Token is null")))
                return@channelFlow
            }
            // todo: fix caffeine dependency issue
            val client = TwitchClientBuilder.builder().apply {
                withChatAccount(OAuth2Credential("twitch", userToken))
                withEnableChat(true)
            }.build()
            client.chat.joinChannel(channel)
            val messages = LinkedList<ChatMessage>()
            client.chat.eventManager.onEvent(AbstractChannelEvent::class.java) { event ->
                Log.d(TAG, "Event: $event")
                when (event) {
                    is ChannelMessageEvent -> {
                        messages.add(0, ChatMessage(event.user.name, event.message))
                        if (messages.size > 100) {
                            messages.removeLast()
                        }
                        trySend(ChatStatus.Connected(messages))
                    }
                }
            }
            awaitClose {
                client.close()
            }
        }.flowOn(Dispatchers.IO)
    }
}